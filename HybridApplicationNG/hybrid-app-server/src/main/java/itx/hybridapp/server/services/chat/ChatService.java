package itx.hybridapp.server.services.chat;

import javax.ejb.Local;

import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatHistoryRequest;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatHistoryResponse;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatListResponse;
import itx.hybridapp.common.protocols.ChatServiceProtocol.ChatPublishEvent;

@Local
public interface ChatService {
	
	public ChatListResponse getList();
	
	public ChatHistoryResponse getHistory(ChatHistoryRequest request);
	
	public void publishMessage(ChatPublishEvent message);
	
	public void publishChatListResponse(String wsSessionId);
	
	public void publishChatHistoryResponse(String wsSessionId, ChatHistoryRequest request);
	
}
