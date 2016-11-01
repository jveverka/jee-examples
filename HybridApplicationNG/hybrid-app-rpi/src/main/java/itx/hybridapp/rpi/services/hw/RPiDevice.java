package itx.hybridapp.rpi.services.hw;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;

public interface RPiDevice {
	
	public GetStatusResponse getStatus(String deviceId, String replyToWsSessionId);
	
	public SensorEvent getSensorData();
	
	public void setControlOutput(SetControlOutputRequest value);
	
	public void setControlOutputEventListener(ControlOutputEventListener listener);
	
	public void setButtonEventListener(ButtonEventListener listener);
	
	public void scanHardware();
	
	public void shutdown();

}
