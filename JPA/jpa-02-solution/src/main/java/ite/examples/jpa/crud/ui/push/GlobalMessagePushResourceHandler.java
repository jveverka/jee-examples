package ite.examples.jpa.crud.ui.push;

import ite.examples.jpa.crud.dto.MessageTO;

import java.util.logging.Logger;

import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PushEndpoint;

/**
 * This class must be in war file directly, not in jar file
 * in case this class is in jar file within war, Primefaces push will not work !
 * @author jveverka
 *
 */

@PushEndpoint(EventDispatcher.PFPUSH_USER_CHANNEL)
public class GlobalMessagePushResourceHandler {

	private static final Logger logger = Logger.getLogger(GlobalMessagePushResourceHandler.class.getName());

	@OnOpen
	public void onOpen(RemoteEndpoint r, EventBus eventBus) {
		logger.info(EventDispatcher.PFPUSH_USER_CHANNEL + " - onOpen");
	}

	@OnClose
	public void onClose(RemoteEndpoint r, EventBus eventBus) {
		logger.info(EventDispatcher.PFPUSH_USER_CHANNEL + " - onClose");
	}
	
	@OnMessage(encoders = {MessageDataMessageEncoder.class}, decoders = {MessageDataMessageDecoder.class})
	public MessageTO onMessage(MessageTO messageData) {
		return messageData;
	}
}
