package itx.hybridapp.javafx.homeform;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import itx.hybridapp.javafx.Scenes;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.EchoMessageEvent;
import itx.hybridapp.javafx.messaging.events.LoginEvent;
import itx.hybridapp.javafx.messaging.events.LogoutEvent;
import itx.hybridapp.javafx.messaging.events.SceneEnterEvent;
import itx.hybridapp.javafx.messaging.events.SceneLeaveEvent;
import itx.hybridapp.javafx.messaging.events.WSConnectEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import net.engio.mbassy.listener.Handler;

public class HomeFormController implements Initializable {
	
	private static final Logger logger = Logger.getLogger(HomeFormController.class.getName());
	private static HomeFormController SELF;
	
	@FXML private Pane homePane;
	@FXML private Label userNameLabel;
	@FXML private Label httpSessionLabel;
	@FXML private Label wsSessionLabel;
	@FXML private Label testResuestsLabel;
	
	private int echoMessageCounter;
	
	public HomeFormController() {
		SELF = this;
		echoMessageCounter = 0;
	}
	
	public static HomeFormController getInstance() {
		return SELF;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		testResuestsLabel.setText(Integer.toString(echoMessageCounter));
		Messaging.getInstance().subscribe(this);
	}
	
	@Handler
	public void onLoginEvent(LoginEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				LoginEvent loginEvent = (LoginEvent) event;
				userNameLabel.setText(loginEvent.getUserName());
				httpSessionLabel.setText(loginEvent.getHttpSessionId());
			}
		});
	}
	
	@Handler
	public void onWsConnectEvent(WSConnectEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				WSConnectEvent wse = (WSConnectEvent) event;
				wsSessionLabel.setText(wse.getWsSessionId());
			}
		});
	}

	@Handler
	public void onEchoMessage(EchoMessageEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				echoMessageCounter++;
				testResuestsLabel.setText(Integer.toString(echoMessageCounter));
			}
		});
	}

	@Handler
	public void onLogoutEvent(LogoutEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				echoMessageCounter = 0;
				userNameLabel.setText("NA");
				httpSessionLabel.setText("NA");
				wsSessionLabel.setText("NA");
				testResuestsLabel.setText(Integer.toString(echoMessageCounter));
			}
		});
	}
	
	@Handler
	public void onSceneEnter(SceneEnterEvent event) {
		if (Scenes.HOME.equals(event.getSceneId())) {
			logger.info("onSceneEnter: HOME");
		}
	}
	
	@Handler 
	public void onSceneLeave(SceneLeaveEvent event) {
		if (Scenes.HOME.equals(event.getSceneId())) {
			logger.info("onSceneLeave: HOME");
		}
	}
	
}
