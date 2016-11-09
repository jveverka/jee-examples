package itx.hybridapp.javafx.loginform;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.LoginEvent;
import itx.hybridapp.javafx.messaging.events.LoginFailedEvent;
import itx.hybridapp.javafx.messaging.events.LogoutEvent;
import itx.hybridapp.javafx.services.ConfigService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.engio.mbassy.listener.Handler;

public class LoginController implements Initializable {

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());
	private static LoginController SELF;

	@FXML private Button loginButton;
	@FXML private TextField loginUserName;
	@FXML private PasswordField loginPassword;
	@FXML private Label loginLabel;
	@FXML private Label versionLabel;
	@FXML private Label loginStatus;
	@FXML private ComboBox<String> protocolSelector;
	
	private String selectedProtocol;
	
	public LoginController() {
		SELF = this;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("init ...");
		Messaging.getInstance().subscribe(this);
		loginButton.setOnAction(new LoginHandler(this));
		versionLabel.setText(ConfigService.getInstance().getAPIVersion());
		protocolSelector.getItems().addAll(ProtoMediaType.APPLICATION_PROTOBUF, ProtoMediaType.APPLICATION_JSON);
		protocolSelector.setOnAction(new ProtocolSelectorHandler(this));
		SELF = this;
	}

	@Handler
	public void onLoginFailedEvent(LoginFailedEvent event) {
		logger.info("onLoginFailedEvent");
		loginStatus.setText(event.getMessage());
	}

	@Handler
	public void onLoginEvent(LoginEvent event) {
		logger.info("onLoginEvent");
		loginStatus.setText("Login OK.");
	}

	@Handler
	public void onLogoutEvent(LogoutEvent event) {
		loginStatus.setText("");
	}
	
	protected String getUserName() {
		return loginUserName.getText();
	}

	protected String getPassword() {
		return loginPassword.getText();
	}

	protected String getProtocol() {
		if (selectedProtocol == null) {
			return ProtoMediaType.getProtocol(true);
		}
		return selectedProtocol;
	}
	
	protected void setSelectedProtocol(String selectedProtocol) {
		this.selectedProtocol = selectedProtocol;
	}

}
