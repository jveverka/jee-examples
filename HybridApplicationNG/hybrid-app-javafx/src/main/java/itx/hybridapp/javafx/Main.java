package itx.hybridapp.javafx;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.LoginEvent;
import itx.hybridapp.javafx.messaging.events.LoginFailedEvent;
import itx.hybridapp.javafx.messaging.events.LogoutEvent;
import itx.hybridapp.javafx.messaging.events.SceneEnterEvent;
import itx.hybridapp.javafx.messaging.events.SceneLeaveEvent;
import itx.hybridapp.javafx.services.ConfigService;
import itx.hybridapp.javafx.services.UserAccessService;
import itx.hybridapp.javafx.services.dto.UserInfo;
import itx.hybridapp.javafx.websockets.WSService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.engio.mbassy.listener.Handler;

public class Main extends Application {
	
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	private static Main SELF;
	
	private Scene mainScene;
	private Stage primaryStage;
	private Scene loginScene;
	
	private BorderPane mainPage;
	private Pane homePage;
	private Pane devicesPage;
	private Pane deviceDetailsPage;
	
	private Scenes currentScene;
	
	private UserAccessService uaService;
	private WSService wsService;
	private String mediaType;
	
	public static Main getInstance() {
		return SELF;
	}
	
	public static void main(String[] args) {
		ConfigService config = ConfigService.getInstance();
		logger.info("Main start: ");
		logger.info("REST URL : " + config.getBaseURL());
		logger.info("WebSocket: " + config.getWsBaseUrl());
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		logger.info("start");
		SELF = this;
		currentScene = Scenes.HOME;
		Messaging.getInstance().subscribe(this);

		this.primaryStage = primaryStage;
		
		Pane loginPage = (Pane) FXMLLoader.load(Main.class.getResource("/itx/hybridapp/javafx/loginform/loginForm.fxml"));
        loginScene = new Scene(loginPage);
        loginScene.getStylesheets().add(Main.class.getResource("/itx/hybridapp/javafx/loginform/loginForm.css").toExternalForm());

        homePage = (Pane) FXMLLoader.load(Main.class.getResource("/itx/hybridapp/javafx/homeform/homeForm.fxml"));
        homePage.getStylesheets().add(Main.class.getResource("/itx/hybridapp/javafx/homeform/homeForm.css").toExternalForm());
        devicesPage = (Pane) FXMLLoader.load(Main.class.getResource("/itx/hybridapp/javafx/devicesform/devicesForm.fxml"));
        devicesPage.getStylesheets().add(Main.class.getResource("/itx/hybridapp/javafx/devicesform/devicesForm.css").toExternalForm());
        deviceDetailsPage = (Pane) FXMLLoader.load(Main.class.getResource("/itx/hybridapp/javafx/devicesform/details/deviceDetailsForm.fxml"));
        deviceDetailsPage.getStylesheets().add(Main.class.getResource("/itx/hybridapp/javafx/devicesform/details/deviceDetailsForm.css").toExternalForm());
        
        mainPage = (BorderPane) FXMLLoader.load(Main.class.getResource("/itx/hybridapp/javafx/mainform/mainForm.fxml"));
        mainPage.getStylesheets().add(Main.class.getResource("/itx/hybridapp/javafx/mainform/mainForm.css").toExternalForm());
        mainPage.setCenter(homePage);
        mainScene = new Scene(mainPage);
        
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Java FX client");
        primaryStage.show();
        primaryStage.setOnCloseRequest(new AppCloseHandler());
	}
	
	private void setLoginScene() {
		primaryStage.setScene(loginScene);
		primaryStage.setResizable(false);
	}

	private void setMainScene() {
		currentScene = Scenes.HOME;
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(true);
	}
	
	public void doLoginAction(String mediaType, String userName, String password) {
		try {
			logger.info("doLoginAction: " + mediaType + " " + userName);
			this.mediaType = mediaType;
			String baseUrl = ConfigService.getInstance().getBaseURL();
			String wsUrl = ConfigService.getInstance().getWsUrl();
			logger.info("HTTP URL: " + baseUrl);
			logger.info("ws URL  : " + wsUrl);
			uaService = UserAccessService.getNewInstance(baseUrl, mediaType);
			List<String> roles = uaService.login(userName, password);
			logger.severe("doLoginAction: OK");
			roles.forEach(r -> {logger.info(r);});
			wsService = WSService.getNewInstance(wsUrl, mediaType);
			wsService.connect();
			setMainScene();
			UserInfo ui = uaService.getUserInfo();
			LoginEvent loginEvent = LoginEvent.newBuilder()
					.setUserName(ui.getUserName())
					.setMediaType(mediaType)
					.setHttpSessionId(ui.getNormalizedHttpSessionId())
					.build();
			Messaging.getInstance().postNow(loginEvent);
		} catch (LoginException e) {
			logger.severe("doLoginAction: FAILED");
			Messaging.getInstance().postNow(LoginFailedEvent.newBuilder()
					.setMessage("invalid credentials")
					.build());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "doLoginAction: FAILED", e);
			Messaging.getInstance().postNow(LoginFailedEvent.newBuilder()
					.setMessage(e.getMessage())
					.build());
		}
	}

	public void doLogout() {
		logger.info("doLogout ...");
		uaService.logout();
		wsService.close();
	}

	public void doLogoutActionSilent() {
		logger.info("doLogoutActionSilent ...");
		doLogout();
		setLoginScene();
	}

	public void doLogoutAction() {
		logger.info("doLogoutAction ...");
		doLogoutActionSilent();
		Messaging.getInstance().postNow(LogoutEvent.newBuilder().build());
	}
	
	public String getMediaType() {
		return mediaType;
	}
	
	public UserInfo getUserInfo() {
		return uaService.getUserInfo();
	}
	
	public void switchScenes(Scenes sceneId, Object context) {
		if (currentScene.equals(sceneId)) return;
		logger.info("switchScenes: " + currentScene.name() + " -> " + sceneId.name());
		Messaging.getInstance().postNow(SceneLeaveEvent.newBuilder().setSceneId(currentScene).build());
		currentScene = sceneId;
		switch (sceneId) {
		case HOME:
			mainPage.setCenter(homePage);
			break;
		case DEVICES:
			mainPage.setCenter(devicesPage);
			break;
		case DEVICE_DETAILS:
			mainPage.setCenter(deviceDetailsPage);
			break;
		default:
			throw new UnsupportedOperationException("unsupported sceneId: " + sceneId.name());
		}
		Messaging.getInstance().postNow(SceneEnterEvent.newBuilder().setSceneId(sceneId).setSceneContext(context).build());
	}
	
	@Handler
	public void onLogoutEvent(LogoutEvent event) {
		mainPage.setCenter(homePage);
	}
	
	public void sendMessage(WrapperMessage wm) {
		wsService.sendMessage(wm);
	}
	
}
