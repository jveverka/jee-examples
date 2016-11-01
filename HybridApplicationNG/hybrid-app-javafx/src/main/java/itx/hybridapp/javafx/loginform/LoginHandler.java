package itx.hybridapp.javafx.loginform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LoginHandler implements EventHandler<ActionEvent> {
	
	@Override
	public void handle(ActionEvent event) {
		LoginController.getInstance().doLoginAction();
	}

}
