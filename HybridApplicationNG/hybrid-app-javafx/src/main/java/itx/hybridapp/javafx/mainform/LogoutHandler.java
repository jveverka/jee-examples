package itx.hybridapp.javafx.mainform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LogoutHandler implements EventHandler<ActionEvent> {
	
	@Override
	public void handle(ActionEvent event) {
		MainFormController.getInstance().doLogoutAction();
	}

}
