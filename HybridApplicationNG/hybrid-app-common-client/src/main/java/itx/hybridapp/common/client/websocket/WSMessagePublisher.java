package itx.hybridapp.common.client.websocket;

import java.io.IOException;

import com.google.protobuf.Message;

/**
 * web socket message publisher
 * @author gergej
 *
 */
public interface WSMessagePublisher {
	
	/**
	 * send message to web socket session
	 * @param wm
	 * @throws IOException
	 */
	public void sendMessage(Message wm) throws IOException;
	
	public void login(String protocol, String userName, String password) throws IOException;
	
	public void login(String httpSessionId, String protocol, String userName, String password) throws IOException;
	
	public void close();

}
