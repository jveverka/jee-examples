package ite.examples.jpa.crud.ui;

import ite.examples.jpa.crud.dto.MessageTO;
import ite.examples.jpa.crud.ui.push.EventDispatcher;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="UsersTopic"), 
		//@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jms/topic/TestTopic"),
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jboss/exported/jms/topic/UsersTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic") 
}, mappedName = "UsersTopic")
public class MDrivenTopicBean implements MessageListener {

	private static final Logger logger = Logger.getLogger(MDrivenTopicBean.class.getName());
	
	@Inject
	private EventDispatcher eventDispatcher;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@Override
	public void onMessage(Message message) {
		logger.info("on Topic message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				logger.info("got object " + obj.getClass().getName());
				MessageTO mto = (MessageTO)obj;
				logger.info("firing PF Push message: " + mto.getMessage());
				eventDispatcher.fireMessageDataEvent(mto);
				return;
			} else {
				logger.info("unknown message received !");
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
