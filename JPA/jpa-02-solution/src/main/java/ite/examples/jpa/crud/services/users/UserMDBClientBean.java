package ite.examples.jpa.crud.services.users;

import ite.examples.jpa.crud.dto.MessageTO;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Topic;

import java.util.logging.Logger;


//this annotation creates JMS destinations during deployment
//if not defined here, JMS destinations must be created in standalone-full.xml file
@JMSDestinationDefinitions(
  value = {
    @JMSDestinationDefinition(
      //name = "java:/jms/topic/TestTopic",
      name = "java:/jboss/exported/jms/topic/UsersTopic",		
      interfaceName = "javax.jms.Topic",
      destinationName = "UsersTopic"
    )
  }
)
@Stateless
public class UserMDBClientBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserMDBClientBean.class.getName());
	
	@Inject
	private JMSContext context;
	
	//@Resource(lookup = "java:/jms/topic/TestTopic")
	@Resource(lookup = "java:/jboss/exported/jms/topic/UsersTopic")
	private Topic topic;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	public void sendTopicMessageAction(String message) {
		logger.info("sending topic message ...");
		context.createProducer().send(topic, new MessageTO(message));
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
