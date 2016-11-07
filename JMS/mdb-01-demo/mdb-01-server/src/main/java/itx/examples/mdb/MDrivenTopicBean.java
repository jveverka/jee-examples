package itx.examples.mdb;

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

import itx.examples.mdb.utils.BlackBoxBean;


@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="TestTopic"), 
		//@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jms/topic/TestTopic"),
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jboss/exported/jms/topic/TestTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic") 
}, mappedName = "TestTopic")
public class MDrivenTopicBean implements MessageListener {

	private static final Logger logger = Logger.getLogger(MDrivenTopicBean.class.getName());
	
	@Inject 
	private BlackBoxBean blackBox;
	
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
				logger.info("message: " + mto.getMessage());
				blackBox.addMessageFromTopic(mto.getMessage());
				return;
			}
			blackBox.addMessageFromTopic("received unknown topic message type");
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
