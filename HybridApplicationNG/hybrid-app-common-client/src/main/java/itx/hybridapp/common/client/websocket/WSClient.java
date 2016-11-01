package itx.hybridapp.common.client.websocket;

import java.io.IOException;

import com.google.protobuf.Message;

public interface WSClient {
	
	public static WSClient buildClient(String socketUrl, String mediaType, WSEventListener eventListener) {
		WSClient client = new WSClientImpl(socketUrl, mediaType, eventListener);
		return client;
	}
	
	public void startClient() throws Exception;
	
	public void startClientBlocking() throws Exception;
	
	public void sendMessage(Message wm) throws IOException;
	
	public void close();
	
	public void shutdown();

}
