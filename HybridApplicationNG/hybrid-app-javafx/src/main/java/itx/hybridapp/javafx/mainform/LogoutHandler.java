package itx.hybridapp.javafx.mainform;

import itx.hybridapp.javafx.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LogoutHandler implements EventHandler<ActionEvent> {
	
	@Override
	public void handle(ActionEvent event) {
		Main.getInstance().doLogoutAction();
	}

}
