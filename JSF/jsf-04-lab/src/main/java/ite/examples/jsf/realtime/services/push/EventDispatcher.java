package ite.examples.jsf.realtime.services.push;

import javax.ejb.Local;

@Local
public interface EventDispatcher {
	
	public static final String PFPUSH_GLOBAL_CHANNEL = "/globalPush";

	public void fireMessageDataEvent(EventMessage eventMessage);
	
}
