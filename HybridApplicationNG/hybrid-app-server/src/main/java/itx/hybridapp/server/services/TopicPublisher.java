package itx.hybridapp.server.services;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Topic;

import itx.hybridapp.server.services.dto.WsSessionMessage;

@JMSDestinationDefinitions(
		  value = {
		    @JMSDestinationDefinition(
		      name = "java:/jboss/exported/jms/topic/HybridWSTopic",		
		      interfaceName = "javax.jms.Topic",
		      destinationName = "HybridWSTopic"
		    )
		  }
)
@Stateless
public class TopicPublisher implements Serializable {
	
	private static final Logger logger = Logger.getLogger(TopicPublisher.class.getName());

	@Inject
	private JMSContext context;
	
	@Resource(lookup = "java:/jboss/exported/jms/topic/HybridWSTopic")
	private Topic topic;

	public void publish(WsSessionMessage message) {
		logger.info("sending topic message ...");
		context.createProducer().send(topic, message);
	}

}
