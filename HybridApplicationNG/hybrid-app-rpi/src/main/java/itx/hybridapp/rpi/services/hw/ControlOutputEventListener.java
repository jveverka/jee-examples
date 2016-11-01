package itx.hybridapp.rpi.services.hw;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.ControlOutputEvent;

public interface ControlOutputEventListener {
	
	public void onControlStatusChange(ControlOutputEvent event);
}
