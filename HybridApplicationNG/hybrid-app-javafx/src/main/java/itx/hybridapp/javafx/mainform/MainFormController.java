package itx.hybridapp.javafx.mainform;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import itx.hybridapp.javafx.Main;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.LoginEvent;
import itx.hybridapp.javafx.messaging.events.LogoutEvent;
import itx.hybridapp.javafx.messaging.events.WSDisconnectEvent;
import itx.hybridapp.javafx.services.UserAccessService;
import itx.hybridapp.javafx.websockets.WSService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import net.engio.mbassy.listener.Handler;


public class MainFormController implements Initializable {
	
	private static final Logger logger = Logger.getLogger(MainFormController.class.getName());
	
	private static MainFormController SELF;

	@FXML private BorderPane containerPane;
	@FXML private Button homeButton;
	@FXML private Button devicesButton;
	@FXML private Button logoutButton;
	
	public static MainFormController getInstance() {
		return SELF;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("initialize ...");
		SELF = this;
		Messaging.getInstance().subscribe(this);
		logoutButton.setOnAction(new LogoutHandler());
		homeButton.setOnAction(new HomeButtonHandler());
		devicesButton.setOnAction(new DevicesButtonHandler());
	}
	
	protected void doLogoutAction() {
		logger.info("doLogoutAction ...");
		try {
			UserAccessService.getInstance().logout();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
		WSService.getInstance().close();
		Main.getInstance().setLoginScene();
		Messaging.getInstance().postNow(LogoutEvent.newBuilder().build());
	}

	@Handler
	public void onWsDisconnect(WSDisconnectEvent event) {
		logger.info("onWsDisconnect ...");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					UserAccessService.getInstance().logout();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "", e);
				}
				Main.getInstance().setLoginScene();
			}
		});
	}
	
}
