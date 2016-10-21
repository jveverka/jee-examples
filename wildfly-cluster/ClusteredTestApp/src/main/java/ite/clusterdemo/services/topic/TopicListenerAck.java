package ite.clusterdemo.services.topic;

import javax.ejb.MessageDriven;
import javax.inject.Inject;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.wsrest.WsResponseCache;

/**
 * requires entry in standalone-full.xml
 *  <jms-topic name="TestClusterTopic">
 *     <entry name="java:/jboss/exported/jms/topic/TestClusterTopic"/>
 *  </jms-topic>
 * @author gergej
 *
 */

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="TestClusterTopic"), 
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jboss/exported/jms/topic/TestClusterTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "MESSAGE_TYPE = 'DATA_MESSAGE_ACK'")
}, mappedName = "TestClusterTopic")
public class TopicListenerAck implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(TopicListenerAck.class.getName());
	
	@Inject
	private WsResponseCache responseCache;

	@Override
	public void onMessage(Message message) {
		logger.info("on Topic ACK message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				logger.info("got object " + obj.getClass().getName());
				DataMessage dataMessage = (DataMessage)obj;
				responseCache.setResponse(dataMessage);
				return;
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

}
