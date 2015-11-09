package ite.clusterdemo.services.topic;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import ite.clusterdemo.services.dto.DataMessage;

@Stateless
public class TopicPublisherAck {

	private static final Logger logger = Logger.getLogger(TopicPublisherAck.class.getName());

	@Inject
	private JMSContext context;
	
	@Resource(lookup = "java:/jboss/exported/jms/topic/TestClusterTopic")
	private Topic topic;

	@Asynchronous
	public void publishAckMessage(DataMessage dataMessage) {
		logger.info("publish ACK message " + dataMessage.toString());
		try {
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(dataMessage);
			objectMessage.setStringProperty("MESSAGE_TYPE", "DATA_MESSAGE_ACK");
			context.createProducer().send(topic, objectMessage);
		} catch (JMSException e) {
			logger.severe("publishMessage: JMSException");
		}
	}

}
