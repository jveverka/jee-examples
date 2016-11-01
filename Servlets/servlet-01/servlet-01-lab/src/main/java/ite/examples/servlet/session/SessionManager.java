package ite.examples.servlet.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;


@Singleton
@Startup
public class SessionManager {
	
	private static final Logger logger = Logger.getLogger(SessionManager.class.getName());
	private static final int SESSTION_TIMEOUT = 20*1000;
	private Map<String, HttpSession> sessions;
	private Map<String, Long> sessionDates;
	
	@PostConstruct
	private void init() { 
		logger.info("init ...");
		sessions = new HashMap<>();
		sessionDates = new HashMap<>();
	}

	public boolean checkSession(HttpSession session) {
		if (sessions.get(session.getId()) == null) {
			sessions.put(session.getId(), session);
			sessionDates.put(session.getId(), new Long(System.currentTimeMillis()));
			return false;
		}
		return true;
	}
	
	@Schedule(second="*/20", minute="*", hour="*", persistent=false )
	public void sessionCleanup() {
		long now = System.currentTimeMillis();
		List<String> sessionsToRemove = new ArrayList<>();
		for (String id: sessionDates.keySet()) {
			if ((sessionDates.get(id) + SESSTION_TIMEOUT) < now) {
				logger.info("invalidating expired http session: " + id);
				sessionsToRemove.add(id);
			}
		}
		for (String id: sessionsToRemove) {
			sessionDates.remove(id);
			sessions.get(id).invalidate();
			sessions.remove(id);
		}
	}
	
}
