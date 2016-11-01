package ite.examples.ejb.batch.ui.push;

import ite.examples.ejb.batch.dto.MessageTO;

import javax.ejb.Local;

@Local
public interface EventDispatcher {
	
	public static final String PFPUSH_USER_CHANNEL = "/userChannelPush";

	public void fireMessageDataEvent(MessageTO eventMessage);
	
}
