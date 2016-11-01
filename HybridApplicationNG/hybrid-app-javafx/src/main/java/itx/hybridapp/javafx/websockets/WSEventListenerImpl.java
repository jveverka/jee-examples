package itx.hybridapp.javafx.websockets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.client.websocket.WSEventListener;
import itx.hybridapp.common.client.websocket.WSMessagePublisher;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceInfo;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataResponse;
import itx.hybridapp.javafx.messaging.Messaging;
import itx.hybridapp.javafx.messaging.events.DeviceStatusEvent;
import itx.hybridapp.javafx.messaging.events.DeviceDataEvent;
import itx.hybridapp.javafx.messaging.events.DeviceInfoListChangedEvent;
import itx.hybridapp.javafx.messaging.events.DeviceInfoListEvent;
import itx.hybridapp.javafx.messaging.events.EchoMessageEvent;
import itx.hybridapp.javafx.messaging.events.TimeSeriesDataEvent;
import itx.hybridapp.javafx.messaging.events.WSConnectEvent;
import itx.hybridapp.javafx.messaging.events.WSDisconnectEvent;
import itx.hybridapp.javafx.services.UserAccessService;
import itx.hybridapp.javafx.services.dto.UserInfo;

public class WSEventListenerImpl implements WSEventListener {
	
	private static final Logger logger = Logger.getLogger(WSEventListenerImpl.class.getName());
	
	private WSMessagePublisher messagePublisher;
	private AtomicBoolean isAuthorized = new AtomicBoolean(false);

	@Override
	public void onInit(WSMessagePublisher messagePublisher) {
		logger.info("onInit");
		this.messagePublisher = messagePublisher;
		this.isAuthorized.set(false);
	}

	@Override
	public void onMessage(WrapperMessage wm) {
		logger.info("onMessage");
		try {
			int msgOrdinal = wm.getMsgCase().getNumber();
			if (WrapperMessage.ECHODATA_FIELD_NUMBER == msgOrdinal) {
				Messaging.getInstance().postNow(EchoMessageEvent.newBuilder().build());
				sendMessage(wm);
			} else if (WrapperMessage.LOGINRESPONSE_FIELD_NUMBER == msgOrdinal) {
				logger.info("login OK");
				wm.getLoginResponse().getRoleList().forEach(r -> {
					logger.info("role: " + new String(r.getBytes(), Charset.forName("UTF-8")));
				});
				this.isAuthorized.set(true);
				Messaging.getInstance().postNow(WSConnectEvent.newBuilder()
						.setWsSessionId(wm.getLoginResponse().getWsSessionId())
						.build()
						);
			} else if (WrapperMessage.DEVICELISTRESPONSE_FIELD_NUMBER == msgOrdinal) {	
				List<DeviceInfo> dil = wm.getDeviceListResponse().getDevicesList();
				DeviceInfoListEvent dilEvent = DeviceInfoListEvent.newBuilder().setDeviceInfoList(dil).build();
				Messaging.getInstance().postNow(dilEvent);
			} else if (WrapperMessage.DEVICELISTCHANGED_FIELD_NUMBER == msgOrdinal) {
				Messaging.getInstance().postNow(DeviceInfoListChangedEvent.newBuilder().build());
			} else if (WrapperMessage.GETSTATUSRESPONSE_FIELD_NUMBER == msgOrdinal) {
				GetStatusResponse statusResponse = wm.getGetStatusResponse();
				DeviceStatusEvent dde = DeviceStatusEvent.newBuilder().setData(statusResponse).build();
				Messaging.getInstance().postNow(dde);
			} else if (WrapperMessage.DEVICEEVENT_FIELD_NUMBER == msgOrdinal) {
				DeviceEvent event = wm.getDeviceEvent();
				DeviceDataEvent dataEvent = DeviceDataEvent.newBuilder().setData(event).build();
				Messaging.getInstance().postNow(dataEvent);
			} else if (WrapperMessage.TIMESERIESDATARESPONSE_FIELD_NUMBER == msgOrdinal) {
				TimeSeriesDataResponse tsdr = wm.getTimeSeriesDataResponse();
				TimeSeriesDataEvent tsde = TimeSeriesDataEvent.newBuilder().setData(tsdr).build();
				Messaging.getInstance().postNow(tsde);
			} else {
				logger.severe("unsupported message: " + msgOrdinal);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException: ", e);
		}
	}

	@Override
	public void onSessionClosed() {
		logger.info("onSessionClosed");
		this.isAuthorized.set(false);
		Messaging.getInstance().postNow(WSDisconnectEvent.newBuilder().build());
	}

	@Override
	public void onSessionCreated(String wsSessionId) {
		logger.info("onSessionCreated");
		UserInfo ui = UserAccessService.getInstance().getUserInfo();
		try {
			messagePublisher.login(ui.getNormalizedHttpSessionId(), ProtoMediaType.APPLICATION_PROTOBUF, ui.getUserName(), ui.getPassword());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public void onSessionError() {
		logger.info("onSessionError");
		this.isAuthorized.set(false);
		Messaging.getInstance().postNow(WSDisconnectEvent.newBuilder().build());
	}

	@Override
	public void onShutdown() {
		logger.info("onShutdown");
		this.isAuthorized.set(false);
	}
	
	public void sendMessage(WrapperMessage wm) throws IOException {
		if (isAuthorized.get()) {
			messagePublisher.sendMessage(wm);
		}
	}

}
