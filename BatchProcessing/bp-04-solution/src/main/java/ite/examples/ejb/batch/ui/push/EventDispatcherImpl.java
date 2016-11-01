package ite.examples.ejb.batch.ui.push;

import java.util.logging.Logger;

import ite.examples.ejb.batch.dto.MessageTO;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@Singleton
@Startup
public class EventDispatcherImpl implements EventDispatcher {
	
	private static final Logger logger = Logger.getLogger(MessageDataMessageDecoder.class.getName());
	private final EventBus eventBus = EventBusFactory.getDefault().eventBus();

	@Override
	public void fireMessageDataEvent(MessageTO eventMessage) {
		logger.info("fireMessageDataEvent: " + eventMessage);
		eventBus.publish(PFPUSH_USER_CHANNEL, eventMessage);
	}

}
