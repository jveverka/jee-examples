package itx.hybridapp.server.services.useraccess;

import javax.ejb.Local;

import com.google.protobuf.Message;

@Local
public interface MessagePublisher {

	/**
	 * publish message to topic. message is send to all websocket clients subscribed to the topic
	 * @param topicId
	 * @param message
	 * @return
	 */
	public int publishToTopic(String topicId, Message message);

	/**
	 * publish message to websocket session. message is send to one websocket session only
	 * @param wsSessionId
	 * @param message
	 * @return
	 */
	public int publishToWsSession(String wsSessionId, Message message);

	/**
	 * publish message to http session session. message is send to all websocket sessions associated with particular http session
	 * @param httpSessionId
	 * @param message
	 * @return
	 */
	public int publishToHttpSession(String httpSessionId, Message message);

	/**
	 * publish message to all websocket sessions
	 * @param message
	 * @return
	 */
	public int publishToAll(Message message);

}
