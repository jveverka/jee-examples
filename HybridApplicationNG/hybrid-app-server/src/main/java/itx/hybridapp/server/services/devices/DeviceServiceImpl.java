package itx.hybridapp.server.services.devices;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceInfo;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListChanged;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorDataRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeDataHolder;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataResponse;
import itx.hybridapp.server.services.useraccess.MessagePublisher;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DeviceServiceImpl implements DeviceService {
	
	private static final Logger logger = Logger.getLogger(DeviceServiceImpl.class.getName());
	
	private Table<String, String, DeviceInfo> devices; //indexed by websocket session id and deviceId

	@Inject 
	private MessagePublisher messagePublisher;

	@Inject
	private DataSeriesBuffer dsBuffer;

	@PostConstruct
	public void init() {
		devices = HashBasedTable.create();
	}

	@Override
	public DeviceListResponse getDevices() {
		logger.info("getDevices");
		return DeviceListResponse.newBuilder().addAllDevices(devices.values()).build();
	}

	@Override
	public void registerDevice(String wsSessionId, String deviceId) {
		logger.info("registerDevice: " + wsSessionId + " : " + deviceId);
		DeviceInfo di = DeviceInfo.newBuilder()
				.setSessionId(wsSessionId).setConnected(System.currentTimeMillis()).setDeviceId(deviceId).build();
		devices.put(wsSessionId, deviceId, di);
		publishDeviceListChanged();
	}

	@Override
	public void unregisterDevice(String wsSessionId) {
		logger.info("unregisterDevice: " + wsSessionId);
		Map<String, DeviceInfo> deviceById = devices.row(wsSessionId);
		deviceById.keySet().forEach( k -> { devices.remove(wsSessionId, k); } );
		publishDeviceListChanged();
	}

	@Override
	public void publishGetDeviceStatus(String deviceId, String replyToWsSessionId) {
		logger.info("publishGetDeviceSatus: " + deviceId);
		Map<String, DeviceInfo> devicesByWsSession = devices.column(deviceId);
		String wsSessionId = devicesByWsSession.keySet().stream().findFirst().get();
		GetStatusRequest dsr = GetStatusRequest.newBuilder()
				.setDeviceId(deviceId)
				.setReplyToWsSessionId(replyToWsSessionId)
				.build();
		WrapperMessage wrapperResponse = WrapperMessage.newBuilder()
				.setGetStatusRequest(dsr)
				.build();
		messagePublisher.publishToWsSession(wsSessionId, wrapperResponse);
	}
	
	@Override
	public void publishDeviceList(String wsSessionId) {
		WrapperMessage deviceResponse = WrapperMessage.newBuilder()
				.setDeviceListResponse(getDevices())
				.build();
		messagePublisher.publishToWsSession(wsSessionId, deviceResponse);
	}

	@Override
	public void publishDeviceStatusResponse(GetStatusResponse dsr) {
		WrapperMessage wrapperResponse = 
				WrapperMessage.newBuilder()
				.setGetStatusResponse(dsr)
				.build();
		messagePublisher.publishToWsSession(dsr.getReplyToWsSessionId(), wrapperResponse);
	}

	@Override
	public void publishSetControlrequest(SetControlOutputRequest scr) {
		WrapperMessage wrapperResponse = 
				WrapperMessage.newBuilder()
				.setSetControlOutputRequest(scr)
				.build();
		Map<String, DeviceInfo> devicesByWsSession = devices.column(scr.getDeviceId());
		String wsSessionId = devicesByWsSession.keySet().stream().findFirst().get();
		messagePublisher.publishToWsSession(wsSessionId,wrapperResponse);
	}

	@Override
	public void publishDeviceEvent(DeviceEvent de) {
		logger.info("publishDeviceEvent: " + de.getDeviceId());
		long timeStamp = System.currentTimeMillis();
		if (de.getMsgCase().getNumber() == DeviceEvent.SENSOREVENT_FIELD_NUMBER) {
			SensorEvent se = SensorEvent.newBuilder()
					.setTimeStamp(timeStamp)
					.setTemperature(de.getSensorEvent().getTemperature())
					.setPressure(de.getSensorEvent().getPressure())
					.setRelativeHumidity(de.getSensorEvent().getRelativeHumidity())
					.build();
			de = DeviceEvent.newBuilder()
					.setDeviceId(de.getDeviceId())
					.setSensorEvent(se)
					.build();
			TimeDataHolder tdh = TimeDataHolder.newBuilder()
					.setTimeStamp(timeStamp)
					.setTemperature(de.getSensorEvent().getTemperature())
					.setRelativeHumidity(de.getSensorEvent().getRelativeHumidity())
					.setPressure(de.getSensorEvent().getPressure())
					.build();
			dsBuffer.addData(de.getDeviceId(), tdh);
		}
		WrapperMessage wrapperResponse = 
				WrapperMessage.newBuilder()
				.setDeviceEvent(de)
				.build();
		messagePublisher.publishToTopic(DeviceService.DEVICE_TOPIC_ID + "/" + de.getDeviceId(), wrapperResponse);
	}

	private void publishDeviceListChanged() {
		WrapperMessage wrapperResponse = 
				WrapperMessage.newBuilder()
				.setDeviceListChanged(DeviceListChanged.newBuilder().build())
				.build();
		messagePublisher.publishToTopic(DeviceService.DEVICE_TOPIC_ID, wrapperResponse);
	}

	@Override
	public void publishSensorDataRequest(SensorDataRequest sdr) {
		logger.info("publishSensorDataRequest: " + sdr.getDeviceId());
		Map<String, DeviceInfo> devicesByWsSession = devices.column(sdr.getDeviceId());
		String wsSessionId = devicesByWsSession.keySet().stream().findFirst().get();
		WrapperMessage wrapperResponse = WrapperMessage.newBuilder()
				.setSensorDataRequest(sdr)
				.build();
		messagePublisher.publishToWsSession(wsSessionId, wrapperResponse);
	}

	@Override
	public void publishTimeSeriesDataResponse(String wsSessionId, TimeSeriesDataRequest tsdRequest) {
		logger.info("publishTimeSeriesDataResponse: " + wsSessionId + " " + tsdRequest.getDeviceId());
		TimeSeriesDataResponse tsdResponse = dsBuffer.getData(tsdRequest.getDeviceId());
		if (tsdResponse != null) {
			WrapperMessage wrapperResponse = WrapperMessage.newBuilder()
					.setTimeSeriesDataResponse(tsdResponse)
					.build();
			messagePublisher.publishToWsSession(wsSessionId, wrapperResponse);
		}
	}
	
	@Schedule(hour = "*", minute = "*", second="*/10", persistent = false)
	public void scheduledPeriodicScanner() {
		Collection<DeviceInfo> allDevices = devices.values();
		allDevices.forEach(d -> { 
			SensorDataRequest sdr = SensorDataRequest.newBuilder()
					.setDeviceId(d.getDeviceId())
					.build();
			WrapperMessage wrapperResponse = WrapperMessage.newBuilder()
					.setSensorDataRequest(sdr)
					.build();
			Map<String, DeviceInfo> devicesByWsSession = devices.column(d.getDeviceId());
			String wsSessionId = devicesByWsSession.keySet().stream().findFirst().get();
			messagePublisher.publishToWsSession(wsSessionId, wrapperResponse);
		});
	}

}
