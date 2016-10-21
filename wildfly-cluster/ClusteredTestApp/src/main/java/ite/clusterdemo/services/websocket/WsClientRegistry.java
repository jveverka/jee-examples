package ite.clusterdemo.services.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.websocket.Session;

import ite.clusterdemo.services.DataLoggerService;
import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.dto.DataMessageType;
import ite.clusterdemo.services.dto.MessageUtils;
import ite.clusterdemo.services.topic.SystemEvent;
import ite.clusterdemo.services.topic.SystemEventType;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class WsClientRegistry {
	
	private static final Logger logger = Logger.getLogger(WsClientRegistry.class.getName());
	
	private Map<String, Session> wsClients;
	
	@Inject
	private DataLoggerService loggerService;
	
	@PostConstruct
	public void init() {
		wsClients = new HashMap<>();
	}
	
	public void addSession(Session session) {
		synchronized(wsClients) { 
			wsClients.put(session.getId(), session);
		}
	}

	public void removeSession(String sessionId) {
		synchronized(wsClients) { 
			wsClients.remove(sessionId);
		}
	}
	
	public boolean sendMessage(String sessionId, DataMessage message) {
		Session session = null;
		synchronized(wsClients) { 
			session = wsClients.get(sessionId);
		}
		if (session != null) {
			session.getAsyncRemote().sendText(MessageUtils.encode(message));
			loggerService.logDataMessage(message);
			logger.info("message send to websocket OK");
			return true;
		} else {
			logger.severe("unknown web socket session Id: " + sessionId);
		}
		return false;
	}
	
	public void notifyAllSessions(SystemEvent systemEvent) {
		synchronized(wsClients) { 
			for (Session session: wsClients.values()) {
				if (session.getId().equals(systemEvent.getSessionId()) 
						&& SystemEventType.userJoined.equals(systemEvent.getType())) {
					//skip user's own joined notification
					continue;
				}
				session.getAsyncRemote().sendText(MessageUtils.encodeSystemEvent(systemEvent));
			}
		}
	}
	
	public DataMessage sendMessageSync(String sessionId, DataMessage message) {
		logger.info("sendMessageSync ...");
		long startTime = System.currentTimeMillis();
		Session session = null;
		synchronized(wsClients) { 
			session = wsClients.get(sessionId);
		}
		if (session != null) {
			session.getAsyncRemote().sendText(MessageUtils.encode(message));
			loggerService.logDataMessage(message);
			String returnMessageId = UUID.randomUUID().toString();
			DataMessage ackDataMessage = new DataMessage(message.getTargetId(), message.getSourceId(), 
					returnMessageId, message.getMessageId(), DataMessageType.dataMessageAck,
					MessageUtils.OK, MessageUtils.createSimpleMessagePayload("message delivery OK"));
			logger.info("sendMessageSync total duration: " + (System.currentTimeMillis() - startTime) + "ms");
			return ackDataMessage;
		} else {
			String returnMessageId = UUID.randomUUID().toString();
			DataMessage ackDataMessage = new DataMessage(message.getTargetId(), message.getSourceId(), 
					returnMessageId, message.getMessageId(), DataMessageType.dataMessageAck,
					MessageUtils.ERROR, MessageUtils.createSimpleMessagePayload("WS session not found !"));
			logger.info("sendMessageSync total duration: " + (System.currentTimeMillis() - startTime) + "ms");
			return ackDataMessage;
		}
	}
	
	public boolean disconnectSession(String sessionId) {
		Session session = null;
		synchronized(wsClients) { 
			session = wsClients.get(sessionId);
		}
		if (session != null) {
			try {
				session.close();
				return true;
			} catch (IOException e) {
			}
		}
		return false;
	}

}
