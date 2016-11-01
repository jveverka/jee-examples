package itx.hybridapp.javafx.devicesform.details;

import itx.hybridapp.javafx.Main;
import itx.hybridapp.javafx.Scenes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class CloseButtonHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		Main.getInstance().switchScenes(Scenes.DEVICES, null);
	}

}
