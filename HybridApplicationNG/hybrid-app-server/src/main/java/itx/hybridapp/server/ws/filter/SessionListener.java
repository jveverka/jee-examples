package itx.hybridapp.server.ws.filter;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import itx.hybridapp.server.services.useraccess.UserAccessService;


@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionIdListener {
	
	private static final Logger logger = Logger.getLogger(SessionListener.class.getName());
	
	@Inject
	private UserAccessService uaService;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("sessionCreated: " + se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		String sessionId = se.getSession().getId();
		logger.info("sessionDestroyed: " + sessionId);
		uaService.logoutHttpSession(sessionId, false);
	}

	@Override
	public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
		logger.info("sessionIdChanged: ");
		uaService.changeHttpSessionId(oldSessionId, event.getSession());
	}

}
