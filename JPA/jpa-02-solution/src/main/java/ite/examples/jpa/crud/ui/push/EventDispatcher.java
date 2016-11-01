package ite.examples.jpa.crud.ui.push;

import ite.examples.jpa.crud.dto.MessageTO;

import javax.ejb.Local;

@Local
public interface EventDispatcher {
	
	public static final String PFPUSH_USER_CHANNEL = "/userChannelPush";

	public void fireMessageDataEvent(MessageTO eventMessage);
	
}
