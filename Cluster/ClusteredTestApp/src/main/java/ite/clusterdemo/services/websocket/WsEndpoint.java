package ite.clusterdemo.services.websocket;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ite.clusterdemo.services.ConfigData;
import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.dto.DataMessageType;
import ite.clusterdemo.services.dto.MessageUtils;
import ite.clusterdemo.services.dto.SessionInfo;
import ite.clusterdemo.services.topic.SystemEvent;
import ite.clusterdemo.services.topic.SystemEventPublisher;
import ite.clusterdemo.services.topic.SystemEventType;
import ite.clusterdemo.services.topic.TopicPublisher;

@ServerEndpoint(value = "/wsendpoint",
decoders = { MessageDecoder.class },
encoders = { MessageEncoder.class })
public class WsEndpoint {
	
	private static final Logger logger = Logger.getLogger(WsEndpoint.class.getName());

	@Inject
	private ConfigData config;
	
	@Inject
	private WsClientRegistry clientRegistry;
	
	@Inject
	private WsClientGlobalRegistry clientGlobalRegistry;
	
	@Inject
	private SystemEventPublisher systemEventPublisher;
	
	@Inject
	private TopicPublisher publisher;

	@OnMessage
    public DataMessage onMessage(DataMessage dataMessage, Session session) {
		if (DataMessageType.getLocalServerInfo.equals(dataMessage.getMessageType())) {
			List<SessionInfo> activeSessions = clientGlobalRegistry.getOtherActiveSessions(session.getId());
			DataMessage replyMessage = MessageUtils.createLocalServerInfoMessage(config.getServerId(), dataMessage.getSourceId(), dataMessage.getMessageId(), config.getHostname(), config.getServerId(), session.getId(), activeSessions);
			return replyMessage;
		} else {
			DataMessage replyMessage = MessageUtils.createAckOKMessage(config.getServerId(), dataMessage.getSourceId(), dataMessage.getMessageId());
			publisher.publishMessage(dataMessage);
			return replyMessage;
		}
	}

	@OnOpen
    public void onOpen(Session session) {
		logger.info("WebSocket opened: " + session.getId());
		clientRegistry.addSession(session);
		clientGlobalRegistry.registerSession(session.getId(), config.getServerId());
		systemEventPublisher.publishEvent(new SystemEvent(SystemEventType.userJoined, session.getId()));
    }
	
	@OnClose
    public void onClose(Session session, CloseReason reason) {
		clientRegistry.removeSession(session.getId());
		clientGlobalRegistry.unregisterSession(session.getId());
		logger.info("Closing a WebSocket due to: " + reason.getCloseCode().getCode() + "/" + reason.getReasonPhrase() + " " + session.getId());
		systemEventPublisher.publishEvent(new SystemEvent(SystemEventType.userDisconnected, session.getId()));
    }
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		try {
			logger.log(Level.SEVERE, "WS: onError", throwable);
			session.close();
		} catch (IOException e) {
			logger.severe("failed to close session " + session.getId());
		}
		clientRegistry.removeSession(session.getId());
		clientGlobalRegistry.unregisterSession(session.getId());
		systemEventPublisher.publishEvent(new SystemEvent(SystemEventType.userConnectionError, session.getId()));
		logger.info("onError: " + session.getId());
	}

}
