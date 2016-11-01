package itx.hybridapp.javafx.messaging;

import java.util.logging.Logger;

import itx.hybridapp.javafx.messaging.events.AppEvent;
import net.engio.mbassy.bus.MBassador;

public class Messaging {
	
	private static final Logger logger = Logger.getLogger(Messaging.class.getName());
	
	private static Messaging SELF = new Messaging();
	
	private MBassador<AppEvent> bus;
	
	private Messaging() {
		logger.info("initializing messaging ...");
		bus = new MBassador<>();
	}
	
	public static Messaging getInstance() {
		return SELF;
	}
	
	public void subscribe(Object listener) {
		logger.info("subscribe: " + listener.getClass().getName());
		bus.subscribe(listener);
	}
	
	public void unsubscribe(Object listener) {
		bus.unsubscribe(listener);
	}

	public void postNow(AppEvent event) {
		bus.post(event).now();
	}

}
