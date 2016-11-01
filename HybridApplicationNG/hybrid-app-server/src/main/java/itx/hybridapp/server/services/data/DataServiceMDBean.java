package itx.hybridapp.server.services.data;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestResponse;
import itx.hybridapp.server.services.dto.WsSessionMessageWrapper;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="HybridWSTopic"), 
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue	= "java:/jboss/exported/jms/topic/HybridWSTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic") 
}, mappedName = "HybridWSTopic")
public class DataServiceMDBean implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(DataServiceMDBean.class.getName());
	
	@Inject
	private DataTestService dataService;
	
	@Override
	public void onMessage(Message message) {
		logger.info("on Topic message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				if (obj instanceof WsSessionMessageWrapper) {
					WsSessionMessageWrapper messageWrapper = (WsSessionMessageWrapper)obj;
					int messageTypeId = messageWrapper.getMessage().getMsgCase().getNumber();
					if (messageTypeId == WrapperMessage.TESTREQUEST_FIELD_NUMBER) {
						logger.info("processing test request");
						String data = messageWrapper.getMessage().getTestRequest().getData();
						dataService.publishGetDataResponse(messageWrapper.getWsSessionId(), data);
					}
				} 
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

}
