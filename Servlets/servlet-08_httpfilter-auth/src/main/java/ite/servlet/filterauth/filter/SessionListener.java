package ite.servlet.filterauth.filter;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import ite.servlet.filterauth.service.AuthService;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionIdListener {
	
	private static final Logger logger = Logger.getLogger(SessionListener.class.getName());
	
	@Inject
	private AuthService authService;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("sessionCreated: " + se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		String sessionId = se.getSession().getId();
		logger.info("sessionCreated: " + sessionId);
		authService.logout(sessionId);
	}

	@Override
	public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
		logger.info("sessionIdChanged: ");
		authService.renameSession(oldSessionId, event.getSession().getId());
	}

}
