package itx.hybridapp.javafx.devicesform.details;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;
import itx.hybridapp.javafx.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class SetControlButtonHandler implements EventHandler<ActionEvent> {
	
	private DeviceDetailsFormController controller;
	private int buttonId;
	
	public SetControlButtonHandler(DeviceDetailsFormController controller, int buttonId) {
		this.controller = controller;
		this.buttonId = buttonId;
	}
	
	public void sendRequest() {
		SetControlOutputRequest request = SetControlOutputRequest.newBuilder()
				.setDeviceId(controller.getDeviceId())
				.setPinId(buttonId)
				.setState(!controller.getControlState(buttonId))
				.build();
		WrapperMessage wm = WrapperMessage.newBuilder()
				.setSetControlOutputRequest(request)
				.build();
		Main.getInstance().sendMessage(wm);
	}

}
