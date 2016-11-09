package itx.hybridapp.server.services.test;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import itx.hybridapp.common.protocols.CommonAccessProtocol.EchoData;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestJobRequest;
import itx.hybridapp.server.services.dto.WsSessionMessageWrapper;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="HybridWSTopic"), 
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue = "java:/jboss/exported/jms/topic/HybridWSTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic") 
}, mappedName = "HybridWSTopic")
public class TestJobManagerMDBean implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(TestJobManagerMDBean.class.getName());
	
	@Inject
	private TestJobManager testJobManager;

	@Override
	public void onMessage(Message message) {
		logger.info("on Topic message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				if (obj instanceof WsSessionMessageWrapper) {
					WsSessionMessageWrapper messageWrapper = (WsSessionMessageWrapper)obj;
					int messageTypeId = messageWrapper.getMessage().getMsgCase().getNumber();
					if (messageTypeId == WrapperMessage.TESTRESULTLISTREQUEST_FIELD_NUMBER) {
						testJobManager.publishTestResultListResponse();
					} else if (messageTypeId == WrapperMessage.TESTJOBREQUEST_FIELD_NUMBER) {
						TestJobRequest tjr = messageWrapper.getMessage().getTestJobRequest();
						testJobManager.submitTestJob(tjr);
					} else if (messageTypeId == WrapperMessage.ECHODATA_FIELD_NUMBER) {
						EchoData echo = messageWrapper.getMessage().getEchoData();
						testJobManager.onTestJobReply(echo);
					} else if (messageTypeId == WrapperMessage.TESTCLEARRESULTLISTREQUEST_FIELD_NUMBER) {
						testJobManager.clearTestResults();
					}
				}
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

}
