package ite.clusterdemo.services.topic;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.dto.DataMessageType;

/**
 * requires entry in standalone-full.xml
 *  <jms-topic name="TestClusterTopic">
 *     <entry name="java:/jboss/exported/jms/topic/TestClusterTopic"/>
 *  </jms-topic>
 * @author gergej
 *
 */

@Stateless
public class TopicPublisher {
	
	private static final Logger logger = Logger.getLogger(TopicPublisher.class.getName());
	
	@Inject
	private JMSContext context;
	
	@Resource(lookup = "java:/jboss/exported/jms/topic/TestClusterTopic")
	private Topic topic;
	
	public void publishMessage(String sourceId, String targetId, String messageId, String contextId, DataMessageType messageType, String returnCode, String payloadData) {
		DataMessage tMessage = new DataMessage(sourceId, targetId, messageId, contextId, messageType, returnCode, payloadData);
		logger.info("publish message " + tMessage.toString());
		try {
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(tMessage);
			objectMessage.setStringProperty("MESSAGE_TYPE", "DATA_MESSAGE");
			context.createProducer().send(topic, objectMessage);
		} catch (JMSException e) {
			logger.severe("publishMessage: JMSException");
		}
	}

	public void publishMessage(DataMessage dataMessage) {
		logger.info("publish message " + dataMessage.toString());
		try {
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(dataMessage);
			objectMessage.setStringProperty("MESSAGE_TYPE", "DATA_MESSAGE");
			context.createProducer().send(topic, objectMessage);
		} catch (JMSException e) {
			logger.severe("publishMessage: JMSException");
		}
	}

}
