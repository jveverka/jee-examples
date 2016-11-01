package ite.examples.jsf.realtime.services.push;

import javax.ejb.Singleton;

import ite.examples.jsf.realtime.services.push.EventMessage;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@Singleton
public class EventDispatcherImpl implements EventDispatcher {
	
	private final EventBus eventBus = EventBusFactory.getDefault().eventBus();

	@Override
	public void fireMessageDataEvent(EventMessage eventMessage) {
		eventBus.publish(PFPUSH_GLOBAL_CHANNEL, eventMessage);
	}

}
