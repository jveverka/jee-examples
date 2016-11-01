package itx.hybridapp.rpi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.google.protobuf.Message;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.client.websocket.WSEventListener;
import itx.hybridapp.common.client.websocket.WSMessagePublisher;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.ButtonEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.ControlOutputEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.RegisterDevice;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorEvent;
import itx.hybridapp.rpi.services.config.ConfigService;
import itx.hybridapp.rpi.services.hw.ButtonEventListener;
import itx.hybridapp.rpi.services.hw.ControlOutputEventListener;
import itx.hybridapp.rpi.services.hw.RPiDevice;
import itx.hybridapp.rpi.services.hw.RPiDeviceFactory;

public class WSEventListenerImpl implements WSEventListener, ControlOutputEventListener, ButtonEventListener {
	
	private static final Logger logger = Logger.getLogger(WSEventListenerImpl.class.getName());
	
	private ConfigService configService;
	private WSMessagePublisher messagePublisher;
	private AtomicBoolean isAuthorized = new AtomicBoolean(false);
	private RPiDevice rpiDevice;
	
	public WSEventListenerImpl(ConfigService configService) {
		this.configService = configService;
		this.rpiDevice = RPiDeviceFactory.getInstance();
		this.rpiDevice.setButtonEventListener(this);
		this.rpiDevice.setControlOutputEventListener(this);
	}

	@Override
	public void onInit(WSMessagePublisher messagePublisher) {
		logger.info("onInit");
		this.messagePublisher = messagePublisher;
		this.isAuthorized.set(false);
	}

	@Override
	public void onMessage(WrapperMessage wm) {
		logger.info("onMessage");
		int msgOrdinal = wm.getMsgCase().getNumber();
		if (WrapperMessage.LOGINRESPONSE_FIELD_NUMBER == msgOrdinal) {
			logger.info("login OK");
			wm.getLoginResponse().getRoleList().forEach(r -> { logger.info("role: " + new String(r.getBytes(), Charset.forName("UTF-8")));} );
			this.isAuthorized.set(true);
			RegisterDevice registerDevice = RegisterDevice.newBuilder().setDeviceId(configService.getDeviceId()).build();
			WrapperMessage wmRequest = WrapperMessage.newBuilder().setRegisterDevice(registerDevice).build();
			sendMessage(wmRequest);
		} else if (this.isAuthorized.get()) {
			if (WrapperMessage.ECHODATA_FIELD_NUMBER == msgOrdinal) {
				sendMessage(wm);
			} else if (WrapperMessage.GETSTATUSREQUEST_FIELD_NUMBER == msgOrdinal) {
		    	logger.info("get status request");
		    	String replyToWsSessionId = wm.getGetStatusRequest().getReplyToWsSessionId();
		    	GetStatusResponse statusResponse = rpiDevice.getStatus(configService.getDeviceId(), replyToWsSessionId);
		    	WrapperMessage wmResponse = WrapperMessage.newBuilder().setGetStatusResponse(statusResponse).build();
		    	sendMessage(wmResponse);
		    } else if (WrapperMessage.SETCONTROLOUTPUTREQUEST_FIELD_NUMBER == msgOrdinal) {	
		    	logger.info("set control output request");
		    	rpiDevice.setControlOutput(wm.getSetControlOutputRequest());
		    } else if (WrapperMessage.SENSORDATAREQUEST_FIELD_NUMBER == msgOrdinal) {
		    	logger.info("ger sensor data request");
		    	SensorEvent se = rpiDevice.getSensorData();
		    	DeviceEvent de = DeviceEvent.newBuilder().setDeviceId(configService.getDeviceId()).setSensorEvent(se).build();
		    	WrapperMessage wmResponse = WrapperMessage.newBuilder().setDeviceEvent(de).build();
		    	sendMessage(wmResponse);
		    }
		} else {
			logger.severe("unsupported message or not authorized: " + msgOrdinal);
		}
	}

	@Override
	public void onSessionClosed() {
		logger.info("onSessionClosed");
		this.isAuthorized.set(false);
		messagePublisher.close();
	}

	@Override
	public void onSessionCreated(String wsSessionId) {
		try {
			logger.info("onSessionCreated");
			messagePublisher.login(ProtoMediaType.APPLICATION_PROTOBUF, configService.getUserName(), configService.getPassword());
			logger.info("login PASSED");
		} catch (IOException e) {
			logger.severe("login FAILED");
			messagePublisher.close();
		}
	}

	@Override
	public void onSessionError() {
		logger.info("onSessionError");
		this.isAuthorized.set(false);
		messagePublisher.close();
	}

	@Override
	public void onShutdown() {
		logger.info("shutting down ...");
		this.isAuthorized.set(false);
		rpiDevice.shutdown();
	}
	
	private void sendMessage(Message message) {
		try {
			messagePublisher.sendMessage(message);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void onButtonEvent(ButtonEvent event) {
		logger.info("onButtonEvent " + event.getButtonId() + ":" + event.getState());
		if(isAuthorized.get()) {
			DeviceEvent deviceEvent = DeviceEvent.newBuilder()
					.setDeviceId(configService.getDeviceId())
					.setButtonEvent(event)
					.build();
			WrapperMessage wm = WrapperMessage.newBuilder()
					.setDeviceEvent(deviceEvent)
					.build();
			sendMessage(wm);
		}
	}

	@Override
	public void onControlStatusChange(ControlOutputEvent event) {
		logger.info("onControlStatusChange " + event.getPinId() + ":" + event.getState());
		if(isAuthorized.get()) {
			DeviceEvent deviceEvent = DeviceEvent.newBuilder()
					.setDeviceId(configService.getDeviceId())
					.setControlOutputEvent(event)
					.build();
			WrapperMessage wm = WrapperMessage.newBuilder()
					.setDeviceEvent(deviceEvent)
					.build();
			sendMessage(wm);
		}
	}

}
