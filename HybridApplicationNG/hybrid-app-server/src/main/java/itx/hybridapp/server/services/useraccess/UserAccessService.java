package itx.hybridapp.server.services.useraccess;

import java.util.List;

import javax.ejb.Local;
import javax.security.auth.login.LoginException;
import javax.websocket.Session;

import itx.hybridapp.server.services.dto.UserInfo;
import itx.hybridapp.server.services.dto.UserWsInfo;

@Local
public interface UserAccessService {
	
	/**
	 * login user by verifying username and password
	 * @param sessionId
	 *   http unique session id of the user
	 * @param userName
	 *   unique user id
	 * @param password
	 *   user's password
	 * @return
	 *   list of user's roles
	 * @throws LoginException
	 *   is thrown in case username and password verification fails
	 */
	public List<String> loginHttpSession(String httpSessionId, String protocol, String userName, String password) throws LoginException;
	
	/**
	 * change http sessionid, called by servlet container when http session id is changed
	 * @param oldSessionId
	 * @param newSessionId
	 */
	public void changeHttpSessionId(String oldSessionId, String newSessionId);
	
	/**
	 * verify if this http session is valid (has been registered using login previously)
	 * @param sessionId
	 * @return
	 */
	public boolean isValidHttpSession(String httpSessionId);

	/**
	 * verify if this websocket session is valid (has been registered using login previously)
	 * @param wsSessionId
	 * @return
	 */
	public boolean isValidWsSession(String wsSessionId);

	/**
	 * get user info for this session 
	 * @param httpSessionId
	 *   http unique session id 
	 * @return
	 *   user info for the session
	 * @throws LoginException
	 *   thrown if the session is not valid
	 */
	public UserInfo getUserHttpSessionInfo(String httpSessionId) throws LoginException;

	/**
	 * get user info for this session
	 * @param wspSessionId
	 *   websocket unique session id  
	 * @return
	 *   user info for the session
	 * @throws LoginException
	 *   thrown if the session is not valid
	 */
	public UserWsInfo getUserWsSessionInfo(String wspSessionId) throws LoginException;

	/**
	 * remove this http session from management
	 * @param sessionId
	 *   http unique session id 
	 */
	public void logoutHttpSession(String httpSessionId);

	/**
	 * login client using websocket session only
	 * @param wsSession
	 * @param userName
	 * @param password
	 * @return
	 * @throws LoginException
	 */
	public List<String> loginWsSession(Session wsSession, String protocol, String userName, String password) throws LoginException;

	/**
	 * login client using websocket session in scope of particular httpsession
	 * @param wsSession
	 * @param httpSessionId
	 * @param userName
	 * @param password
	 * @return
	 * @throws LoginException
	 */
	public List<String> loginWsSession(Session wsSession, String httpSessionId, String protocol, String userName, String password) throws LoginException;

	/**
	 * associate websocket session with httpsession
	 * @param wsSession
	 * @param httpSessionId
	 * @throws LoginException
	 */
	public void addWsSession(Session wsSession, String httpSessionId, String protocol) throws LoginException;

	/**
	 * subscribe websocket session to topic
	 * @param wsSessionId
	 * @param topicId
	 */
	public void subscribe(String wsSessionId, String topicId);

	/**
	 * unsubscribe websocket session from topic
	 * @param wsSessionId
	 * @param topicId
	 */
	public void unSubscribe(String wsSessionId, String topicId);
	
	/**
	 * remove websocket session from management
	 * @param wsSessionId
	 */
	public void removeWsSession(String wsSessionId, boolean closeOnRemove);

}
