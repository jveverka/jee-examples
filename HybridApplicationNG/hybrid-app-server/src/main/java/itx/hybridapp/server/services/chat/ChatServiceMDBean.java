package itx.hybridapp.server.services.chat;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatHistoryRequest;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatPublishEvent;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.server.services.dto.WsSessionMessageWrapper;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName="destinationName", propertyValue="HybridWSTopic"), 
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue = "java:/jboss/exported/jms/topic/HybridWSTopic"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic") 
}, mappedName = "HybridWSTopic")
public class ChatServiceMDBean implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(ChatServiceMDBean.class.getName());
	
	@Inject
	private ChatService chatService;

	@Override
	public void onMessage(Message message) {
		logger.info("on Topic message ...");
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage)message).getObject();
				if (obj instanceof WsSessionMessageWrapper) {
					WsSessionMessageWrapper messageWrapper = (WsSessionMessageWrapper)obj;
					int messageTypeId = messageWrapper.getMessage().getMsgCase().getNumber();
					if (messageTypeId == WrapperMessage.CHATLISTREQUEST_FIELD_NUMBER) {
						chatService.publishChatListResponse(messageWrapper.getWsSessionId());
					} else if (messageTypeId == WrapperMessage.CHATHISTORYREQUEST_FIELD_NUMBER) {
						ChatHistoryRequest chr = messageWrapper.getMessage().getChatHistoryRequest();
						chatService.publishChatHistoryResponse(messageWrapper.getWsSessionId(), chr);
					} else if (messageTypeId == WrapperMessage.CHATPUBLISHEVENT_FIELD_NUMBER) {
						ChatPublishEvent cpe = messageWrapper.getMessage().getChatPublishEvent();
						chatService.publishMessage(cpe);
					}
				}
			}
		} catch (JMSException e) {
			logger.severe("JMSException: " + e.getMessage());
		}
	}

}
