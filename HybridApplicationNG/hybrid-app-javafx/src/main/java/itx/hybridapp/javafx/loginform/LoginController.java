package itx.hybridapp.javafx.loginform;

import java.net.ConnectException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import itx.hybridapp.javafx.Main;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.LoginEvent;
import itx.hybridapp.javafx.services.ConfigService;
import itx.hybridapp.javafx.services.UserAccessService;
import itx.hybridapp.javafx.services.dto.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());
	private static LoginController SELF;

	@FXML private Button loginButton;
	@FXML private TextField loginUserName;
	@FXML private PasswordField loginPassword;
	@FXML private Label loginLabel;
	@FXML private Label versionLabel;
	@FXML private Label loginStatus;

	public static LoginController getInstance() {
		return SELF;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginButton.setOnAction(new LoginHandler());
		versionLabel.setText(ConfigService.getInstance().getAPIVersion());
		SELF = this;
	}

	protected void doLoginAction() {
		try {
			logger.info("doLoginAction ...");
			loginLabel.setText("");
			String userName = loginUserName.getText();
			String password = loginPassword.getText();
			UserAccessService uaService = UserAccessService.getInstance();
			List<String> roles = uaService.login(userName, password);
			logger.severe("doLoginAction: OK");
			roles.forEach(r -> {logger.info(r);});
			Main.getInstance().setMainScene();
			UserInfo ui = uaService.getUserInfo();
			LoginEvent loginEvent = LoginEvent.newBuilder()
					.setUserName(ui.getUserName())
					.setHttpSessionId(ui.getNormalizedHttpSessionId())
					.build();
			loginStatus.setText("Login OK.");
			Messaging.getInstance().postNow(loginEvent);
		} catch (LoginException e) {
			logger.severe("doLoginAction: FAILED");
			loginStatus.setText("ERROR: invalid credentials.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "doLoginAction: FAILED", e);
			loginStatus.setText("ERROR: " + e.getMessage());
		}
	}

}
