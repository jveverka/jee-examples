package itx.hybridapp.server.services.devices;

import javax.ejb.Local;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeDataHolder;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataResponse;

@Local
public interface DataSeriesBuffer {
	
	public void addData(String deviceId, TimeDataHolder timeData);
	
	public TimeSeriesDataResponse getData(String deviceId);

}
