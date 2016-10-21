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
import ite.clusterdemo.services.dto.MessageUtils;
import ite.clusterdemo.services.websocket.WsClientRegistry;

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
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "MESSAGE_TYPE = 'DATA_MESSAGE'")
}, mappedName = "TestClusterTopic")
public class TopicListener implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(TopicListener.class.getName());
	
	@Inject
	private WsClientRegistry clientRegistry;
	
	@Inject 
	private TopicPublisherAck ackPublisher;
	
	@Override
	public void onMessage(Message message) {
		logger.info("on Topic message ...");
		long startTime = System.currentTimeMillis();
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				logger.info("got object " + obj.getClass().getName());
				DataMessage dataMessage = (DataMessage)obj;
				DataMessage ackDataMessage = clientRegistry.sendMessageSync(dataMessage.getTargetId(), dataMessage);
				if (MessageUtils.OK.equals(ackDataMessage.getReturnCode())) {
					ackPublisher.publishAckMessage(ackDataMessage);
				}
				logger.info("onMessage total duration: " + (System.currentTimeMillis() - startTime) + "ms");
				return;
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

}
