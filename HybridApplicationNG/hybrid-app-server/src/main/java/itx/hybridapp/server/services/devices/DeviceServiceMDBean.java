package itx.hybridapp.server.services.devices;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.RegisterDevice;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorDataRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataRequest;
import itx.hybridapp.server.services.dto.WsSessionDestroyedMessage;
import itx.hybridapp.server.services.dto.WsSessionMessageWrapper;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="HybridWSTopic"), 
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue = "java:/jboss/exported/jms/topic/HybridWSTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic") 
}, mappedName = "HybridWSTopic")
public class DeviceServiceMDBean implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(DeviceServiceMDBean.class.getName());
	
	@Inject
	private DeviceService deviceService;
	
	@Override
	public void onMessage(Message message) {
		logger.info("on Topic message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				if (obj instanceof WsSessionMessageWrapper) {
					WsSessionMessageWrapper messageWrapper = (WsSessionMessageWrapper)obj;
					int messageTypeId = messageWrapper.getMessage().getMsgCase().getNumber();
					if (messageTypeId == WrapperMessage.REGISTERDEVICE_FIELD_NUMBER) {
						RegisterDevice registerDevice = messageWrapper.getMessage().getRegisterDevice();
						deviceService.registerDevice(messageWrapper.getWsSessionId(), registerDevice.getDeviceId());
					} else if (messageTypeId == WrapperMessage.DEVICELISTREQUEST_FIELD_NUMBER) {
						deviceService.publishDeviceList(messageWrapper.getWsSessionId());
					} else if (messageTypeId == WrapperMessage.GETSTATUSREQUEST_FIELD_NUMBER) {
						GetStatusRequest dsr = messageWrapper.getMessage().getGetStatusRequest();
						deviceService.publishGetDeviceStatus(dsr.getDeviceId(), messageWrapper.getWsSessionId());
					} else if (messageTypeId == WrapperMessage.GETSTATUSRESPONSE_FIELD_NUMBER) {
						GetStatusResponse dsr = messageWrapper.getMessage().getGetStatusResponse();
						deviceService.publishDeviceStatusResponse(dsr);
					} else if (messageTypeId == WrapperMessage.SETCONTROLOUTPUTREQUEST_FIELD_NUMBER) {
						SetControlOutputRequest scr = messageWrapper.getMessage().getSetControlOutputRequest();
						deviceService.publishSetControlrequest(scr);
					} else if (messageTypeId == WrapperMessage.DEVICEEVENT_FIELD_NUMBER) {
						DeviceEvent de = messageWrapper.getMessage().getDeviceEvent();
						deviceService.publishDeviceEvent(de);
					} else if (messageTypeId == WrapperMessage.SENSORDATAREQUEST_FIELD_NUMBER) {
						SensorDataRequest sdr = messageWrapper.getMessage().getSensorDataRequest();
						deviceService.publishSensorDataRequest(sdr);
					} else if (messageTypeId == WrapperMessage.TIMESERIESDATAREQUEST_FIELD_NUMBER) {
						TimeSeriesDataRequest tsdRequest = messageWrapper.getMessage().getTimeSeriesDataRequest();
						deviceService.publishTimeSeriesDataResponse(messageWrapper.getWsSessionId(), tsdRequest);
					}
				} else if (obj instanceof WsSessionDestroyedMessage) {
					WsSessionDestroyedMessage sessionDestroyed = (WsSessionDestroyedMessage)obj;
					deviceService.unregisterDevice(sessionDestroyed.getWsSessionId());
				}
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

}
