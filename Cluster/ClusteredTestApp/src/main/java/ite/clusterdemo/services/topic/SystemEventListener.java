package ite.clusterdemo.services.topic;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import ite.clusterdemo.services.websocket.WsClientRegistry;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="TestClusterTopic"), 
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jboss/exported/jms/topic/TestClusterTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "MESSAGE_TYPE = 'SYSTEM_EVENT'")
}, mappedName = "TestClusterTopic")
public class SystemEventListener implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(SystemEventListener.class.getName());
	
	@Inject
	private WsClientRegistry clientRegistry;

	@Override
	public void onMessage(Message message) {
		logger.info("onMessage SYSTEM_EVENT ...");
		if (message instanceof ObjectMessage) {
			ObjectMessage objMessage = (ObjectMessage)message;
			try {
				if (objMessage.getObject() instanceof SystemEvent) {
					SystemEvent sysEvent = (SystemEvent)objMessage.getObject();
					logger.info(sysEvent.toString());
					if (SystemEventType.userConnectionError.equals(sysEvent.getType()) ||
							SystemEventType.userJoined.equals(sysEvent.getType()) ||
							SystemEventType.userDisconnected.equals(sysEvent.getType())) {
						clientRegistry.notifyAllSessions(sysEvent);
					} else if (SystemEventType.sessionDisconnectRequest.equals(sysEvent.getType())) {
						clientRegistry.disconnectSession(sysEvent.getSessionId());
					}
				}
			} catch (JMSException e) {
				logger.severe("JMSException: " + e.getMessage());
			}
		}
	}

}
