package ite.examples.mdb;

import ite.examples.mdb.utils.BlackBoxBean;

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
/*@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="TestQueue"), 
		//@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jms/queue/TestQueue"),
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jboss/exported/jms/queue/TestQueue"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue") 
}, mappedName = "TestQueue")*/
public class MDrivenQueueBean implements MessageListener {

	private static final Logger logger = Logger.getLogger(MDrivenQueueBean.class.getName());
	
	@Inject 
	private BlackBoxBean blackBox;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@Override
	public void onMessage(Message message) {
		logger.info("on Queue message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				logger.info("got object " + obj.getClass().getName());
				MessageTO mto = (MessageTO)obj;
				logger.info("message: " + mto.getMessage());
				blackBox.addMessageFromQueue(mto.getMessage());
				return;
			}
			blackBox.addMessageFromQueue("received unknown queue message type");
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
