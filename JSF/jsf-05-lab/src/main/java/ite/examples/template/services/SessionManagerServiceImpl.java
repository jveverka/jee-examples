package ite.examples.template.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SessionManagerServiceImpl implements SessionManagerService {
	
	private static final Logger logger = Logger.getLogger(SessionManagerServiceImpl.class.getName());
	
	private Map<String, SessionRecord> sessions;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		sessions = new HashMap<>();
	}
	
	//@Schedule(second = "*/10", minute="*", hour="*", persistent=false)
	//public void scheduledAction() {
	//	logger.info("scheduledAction ...");
	//}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

	@Override
	public void addSession(SessionRecord sessionRecord) {
		synchronized(sessions) {
			logger.info("session added: " + sessionRecord.toString());
			sessions.put(sessionRecord.getSessionId(), sessionRecord);
		}
	}
	
	@Override
	public void updateSession(String sessionId) {
		synchronized(sessions) {
			logger.info("session updated: " + sessionId);
			SessionRecord sessionRecord = sessions.get(sessionId);
			if (sessionRecord != null) {
				sessionRecord.setLastAccessTime(System.currentTimeMillis());
			}
		}
	}

	@Override
	public boolean isValidSession(String sessionId) {
		return getSessionTimeout(sessionId)>0 ? true:false;
	}

	@Override
	public long getSessionTimeout(String sessionId) {
		synchronized(sessions) {
			SessionRecord sessionRecord = sessions.get(sessionId);
			if (sessionRecord != null) {
				long delta = System.currentTimeMillis() - sessionRecord.getLastAccessTime();
				long maxInactive = sessionRecord.getMaxInactiveInterval() * 1000L;
				logger.info("session: " + sessionId + " delta=" + delta + " maxInactive=" + maxInactive);
				if (delta < maxInactive) {
					return maxInactive - delta;
				} else {
					return -1;
				}
			}
			return -1;
		}
	}

	@Override
	public void removeSession(String sessionId) {
		synchronized(sessions) {
			logger.info("session removed: " + sessionId);
			sessions.remove(sessionId);
		}
	}
	
}
