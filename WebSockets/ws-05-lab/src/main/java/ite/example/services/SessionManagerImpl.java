package ite.example.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.websocket.Session;

import ite.example.services.dto.SessionInfo;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SessionManagerImpl implements SessionManager {

	private static final Logger logger = Logger.getLogger(SessionManager.class.getName());
	
	Map<String, Session> sessions;
	
	@PostConstruct
	private void init() {
		sessions = new HashMap<>();
	}
	
	@Override
	public void addSession(Session session) {
		synchronized(sessions) {
			sessions.put(session.getId(), session);
		}
	}
	
	@Override
	public void removeSession(String sessionId) {
		synchronized(sessions) {
			sessions.remove(sessionId);
		}
	}
	
	@Override
	public void sendMessage(String sessionId, String message) {
		synchronized(sessions) {
			Session session = sessions.get(sessionId);
			if (session != null) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					logger.severe("failed to send message for session " + sessionId);
				}
			} else {
				logger.severe("unknown session " + sessionId);
			}
		}
	}
	
	@Override
	public List<SessionInfo> getActiveSessionInfo() {
		List<SessionInfo> resultSet = new ArrayList<>();
		synchronized(sessions) {
			for (String id: sessions.keySet()) {
				Session session = sessions.get(id);
				resultSet.add(new SessionInfo(session.getId(), session.getMaxIdleTimeout()));
			}
		}
		return resultSet;
	}

}
