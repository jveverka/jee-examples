package itx.hybridapp.rpi.services.hw;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.ButtonEvent;

public interface ButtonEventListener {
	
	public void onButtonEvent(ButtonEvent event);

}
