package ite.examples.servlet.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class SessionAttribute implements HttpSessionBindingListener {
	
	private static final Logger logger = Logger.getLogger(SessionAttribute.class.getName());

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		logger.info("valueBound for session: " + event.getSession().getId());
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		logger.info("valueUnbound for session: " + event.getSession().getId());
	}

}
