package itx.hybridapp.javafx.mainform;

import itx.hybridapp.javafx.Main;
import itx.hybridapp.javafx.Scenes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HomeButtonHandler implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent event) {
		Main.getInstance().switchScenes(Scenes.HOME, null);
	}

}
