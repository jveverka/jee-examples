package itx.examples.mdb.remoteclient;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import itx.examples.mdb.MessageTO;

public class MessageListenerImpl implements MessageListener {

	private static final Logger logger = Logger.getLogger(MessageListenerImpl.class.getName());
	private Object parentProcess;
	
	public MessageListenerImpl(Object parentProcess) {
		this.parentProcess = parentProcess;
	}
	
	@Override
	public void onMessage(Message message) {
		logger.info("on message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				logger.info("got object " + obj.getClass().getName());
				MessageTO mto = (MessageTO)obj;
				logger.info("got message: " + mto.getMessage());
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
		synchronized(parentProcess) {
			parentProcess.notify();
		}
	}

}
