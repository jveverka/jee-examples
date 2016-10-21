package ite.clusterdemo.services.topic;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

@Stateless
public class SystemEventPublisher {
	
	private static final Logger logger = Logger.getLogger(SystemEventPublisher.class.getName());

	@Inject
	private JMSContext context;
	
	@Resource(lookup = "java:/jboss/exported/jms/topic/TestClusterTopic")
	private Topic topic;

	public void publishEvent(SystemEvent systemEvent) {
		try {
			ObjectMessage objMessage = context.createObjectMessage();
			objMessage.setStringProperty("MESSAGE_TYPE", "SYSTEM_EVENT");
			objMessage.setObject(systemEvent); 
			context.createProducer().send(topic, objMessage);
		} catch (JMSException e) {
			logger.severe("publishMessage: JMSException");
		}
	}
}
