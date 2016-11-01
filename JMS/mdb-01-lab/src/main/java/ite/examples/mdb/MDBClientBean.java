package ite.examples.mdb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Queue;
import javax.jms.Topic;

import java.util.logging.Logger;


//this annotation creates JMS destinations during deployment
//if not defined here, JMS destinations must be created in standalone-full.xml file
@JMSDestinationDefinitions(
  value = {
    @JMSDestinationDefinition(
      //name = "java:/jms/queue/TestQueue",
      name = "java:/jboss/exported/jms/queue/TestQueue",
      interfaceName = "javax.jms.Queue",
      destinationName = "TestQueue"
    ),
    @JMSDestinationDefinition(
      //name = "java:/jms/topic/TestTopic",
      name = "java:/jboss/exported/jms/topic/TestTopic",		
      interfaceName = "javax.jms.Topic",
      destinationName = "TestTopic"
    )
  }
)
@Stateless
public class MDBClientBean implements Serializable {

	private static final Logger logger = Logger.getLogger(MDBClientBean.class.getName());
	
	@Inject
	private JMSContext context;
	
	//@Resource(lookup = "java:/jms/topic/TestTopic")
	@Resource(lookup = "java:/jboss/exported/jms/topic/TestTopic")
	private Topic topic;

	//@Resource(lookup = "java:/jms/queue/TestQueue")
	@Resource(lookup = "java:/jboss/exported/jms/queue/TestQueue")
	private Queue queue;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	public void sendTopicMessageAction(String message) {
		logger.info("sending topic message ...");
		context.createProducer().send(topic, new MessageTO(message));
	}

	public void sendQueueMessageAction(String message) {
		logger.info("sending queue message ...");
		context.createProducer().send(queue, new MessageTO(message));
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
