package itx.hybridapp.server.services.devices;

import javax.ejb.Local;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorDataRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataRequest;

@Local
public interface DeviceService {
	
	public final static String DEVICE_TOPIC_ID = "/devices";
	
	public DeviceListResponse getDevices();
	
	public void registerDevice(String wsSessionId, String deviceId);
	
	public void unregisterDevice(String wsSessionId);
	
	public void publishGetDeviceStatus(String deviceId, String replyToWsSessionId);

	public void publishDeviceStatusResponse(GetStatusResponse dsr);

	public void publishDeviceList(String wsSessionId);
	
	public void publishSetControlrequest(SetControlOutputRequest scr);
	
	public void publishDeviceEvent(DeviceEvent de);
	
	public void publishSensorDataRequest(SensorDataRequest sdr);
	
	public void publishTimeSeriesDataResponse(String wsSessionId, TimeSeriesDataRequest tsdRequest);

}
