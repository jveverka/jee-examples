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
	 *   message wrapper
	 * @throws IOException
	 *   thrown in case of IO error
	 */
	public void sendMessage(Message wm) throws IOException;
	
	/**
	 * login using websocket connection
	 * @param protocol
	 *   protocol or mediaType
	 * @param userName
	 *   user name
	 * @param password
	 *   user's password
	 * @throws IOException
	 *   thrown in case of IO error
	 */
	public void login(String protocol, String userName, String password) throws IOException;
	
	/**
	 * login with valid httpSesionId 
	 * @param httpSessionId
	 *   existing valid http session id
	 * @param protocol
	 *   media type
	 * @param userName
	 *   user name
	 * @param password
	 *   user's password
	 * @throws IOException
	 *   thrown in case of IO error
	 */
	public void login(String httpSessionId, String protocol, String userName, String password) throws IOException;
	
	/**
	 * close websocket connection
	 */
	public void close();

}
