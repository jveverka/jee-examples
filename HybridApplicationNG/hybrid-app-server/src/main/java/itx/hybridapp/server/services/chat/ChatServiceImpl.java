package itx.hybridapp.server.services.chat;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatHistoryRequest;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatHistoryResponse;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatListResponse;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatMessage;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatPublishEvent;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.server.services.useraccess.MessagePublisher;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class ChatServiceImpl implements ChatService {
	
	private static final Logger logger = Logger.getLogger(ChatServiceImpl.class.getName());
	private static final String CHAT_TOPIC_BASE = "/chat";
	
	private Map<String, Deque<ChatMessage>> chats;

	@Inject 
	private MessagePublisher messagePublisher;

	@PostConstruct
	public void init() {
		logger.info("init ...");
		chats = new ConcurrentHashMap<>();
	}

	@Override
	public ChatListResponse getList() {
		logger.info("getList");
		ChatListResponse.Builder chatListBuilder = ChatListResponse.newBuilder();
		chats.keySet().forEach( k -> {
			chatListBuilder.addChatId(k);
		});
		return chatListBuilder.build();
	}

	@Override
	public ChatHistoryResponse getHistory(ChatHistoryRequest request) {
		logger.info("getHistory: " + request.getChatId());
		ChatHistoryResponse.Builder historyBuilder = ChatHistoryResponse.newBuilder();
		historyBuilder.setChatId(request.getChatId());
		Deque<ChatMessage> chatHistory = chats.get(request.getChatId());
		chatHistory.forEach(m -> {
			historyBuilder.addMessages(m);
		});
		return historyBuilder.build();
	}

	@Override
	public void publishMessage(ChatPublishEvent message) {
		logger.info("publishMessage: " + message.getChatId());
		Deque<ChatMessage> chatHistory = chats.get(message.getChatId());
		if (chatHistory == null) {
			chatHistory = new ConcurrentLinkedDeque<>();
			chats.put(message.getChatId(), chatHistory);
			WrapperMessage wm = WrapperMessage.newBuilder()
					.setChatListResponse(getList())
					.build();
			messagePublisher.publishToTopic(CHAT_TOPIC_BASE, wm);
		}
		chatHistory.addFirst(message.getMessage());
		WrapperMessage wm = WrapperMessage.newBuilder()
				.setChatPublishEvent(message)
				.build();
		messagePublisher.publishToTopic(CHAT_TOPIC_BASE + "/" + message.getChatId(), wm);
	}

	@Override
	public void publishChatListResponse(String wsSessionId) {
		logger.info("publishChatListResponse");
		WrapperMessage wm = WrapperMessage.newBuilder()
				.setChatListResponse(getList())
				.build();
		messagePublisher.publishToWsSession(wsSessionId, wm);
	}

	@Override
	public void publishChatHistoryResponse(String wsSessionId, ChatHistoryRequest request) {
		logger.info("publishChatHistoryResponse");
		WrapperMessage wm = WrapperMessage.newBuilder()
				.setChatHistoryResponse(getHistory(request))
				.build();
		messagePublisher.publishToWsSession(wsSessionId, wm);
	}

}
