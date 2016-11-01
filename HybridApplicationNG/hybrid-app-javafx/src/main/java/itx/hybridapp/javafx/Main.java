package itx.hybridapp.javafx;

import java.util.logging.Logger;

import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.LogoutEvent;
import itx.hybridapp.javafx.messaging.events.SceneEnterEvent;
import itx.hybridapp.javafx.messaging.events.SceneLeaveEvent;
import itx.hybridapp.javafx.services.ConfigService;
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

		ConfigService config = ConfigService.getInstance();
		WSService.init(config.getWsUrl(), config.getMediaType());
		
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
	
	public void setLoginScene() {
		primaryStage.setScene(loginScene);
		primaryStage.setResizable(false);
	}

	public void setMainScene() {
		currentScene = Scenes.HOME;
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(true);
		try {
			WSService.getInstance().connect();
		} catch (Exception e) {
			logger.info("WS client error !");
		}
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
	
}
