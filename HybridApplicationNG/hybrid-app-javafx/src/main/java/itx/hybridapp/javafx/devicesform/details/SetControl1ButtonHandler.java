package itx.hybridapp.javafx.devicesform.details;

import javafx.event.ActionEvent;

public class SetControl1ButtonHandler extends SetControlButtonHandler {

	public SetControl1ButtonHandler(DeviceDetailsFormController controller, int buttonId) {
		super(controller, buttonId);
	}

	@Override
	public void handle(ActionEvent event) {
		sendRequest();
	}

}
