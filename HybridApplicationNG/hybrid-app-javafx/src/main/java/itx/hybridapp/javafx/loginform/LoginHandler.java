package itx.hybridapp.javafx.loginform;

import itx.hybridapp.javafx.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LoginHandler implements EventHandler<ActionEvent> {
	
	private LoginController loginController;
	
	public LoginHandler(LoginController loginController) {
		this.loginController = loginController;
	}
	
	@Override
	public void handle(ActionEvent event) {
		Main.getInstance().doLoginAction(loginController.getProtocol(), loginController.getUserName(), loginController.getPassword());
	}

}
