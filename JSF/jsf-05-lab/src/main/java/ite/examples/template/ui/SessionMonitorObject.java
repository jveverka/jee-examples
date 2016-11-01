package ite.examples.template.ui;

import ite.examples.template.services.SessionManagerService;
import ite.examples.template.services.SessionRecord;

import java.util.logging.Logger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class SessionMonitorObject implements HttpSessionBindingListener {

	private static final Logger logger = Logger.getLogger(SessionMonitorObject.class.getName());
	private String sessionId;
	private SessionManagerService sessionManager;
	private SessionRecord sessionRecord;
	
	public SessionMonitorObject(String sessionId, SessionManagerService sessionManager, SessionRecord sessionRecord) {	
		this.sessionId = sessionId;
		this.sessionManager = sessionManager;
		this.sessionRecord = sessionRecord;
	}
			
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		logger.info("valueBound: " + sessionId);
		sessionManager.addSession(sessionRecord);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		logger.info("valueUnbound: " + sessionId);
		sessionManager.removeSession(sessionId);
	}

}
