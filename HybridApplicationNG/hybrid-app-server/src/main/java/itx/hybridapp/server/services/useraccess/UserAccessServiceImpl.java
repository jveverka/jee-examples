package itx.hybridapp.server.services.useraccess;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.websocket.Session;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.UserAccessProtocol.HttpSessionInfo;
import itx.hybridapp.common.protocols.UserAccessProtocol.HttpSessionWsSessionsInfo;
import itx.hybridapp.common.protocols.UserAccessProtocol.TopicInfo;
import itx.hybridapp.common.protocols.UserAccessProtocol.UserInfoData;
import itx.hybridapp.common.protocols.UserAccessProtocol.WsSessionInfo;
import itx.hybridapp.server.services.dto.UserInfo;
import itx.hybridapp.server.services.dto.UserWsInfo;
import itx.hybridapp.server.services.dto.WSSessionInfo;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class UserAccessServiceImpl implements UserAccessService, MessagePublisher, UserInfoService {
	
	private static final Logger logger = Logger.getLogger(UserAccessServiceImpl.class.getName());
	
	private static final String USERINFO_TOPICID = "/userinfo";
	
	@Inject
	private UserCredentialsService ucService;
	
	private Map<String, UserInfo> httpSessions;                 //active http sessions by http sessionid
	private Map<String, WSSessionInfo> sessions;                //web-socket sessions by ws session id
	private Map<String, List<String>> wsSessionsForHttpSession; //indexed by http session id
	private Map<String, List<String>> wsSessionsForTopic;       //indexed by topic id
	
	@PostConstruct
	public void init() {
		logger.info("init ...");
		httpSessions = new ConcurrentHashMap<>();
		sessions = new ConcurrentHashMap<>();
		wsSessionsForHttpSession = new ConcurrentHashMap<>();
		wsSessionsForTopic = new ConcurrentHashMap<>();
	}
	
	@PreDestroy
	public void deinit() {
		logger.info("deinit ...");
		sessions.values().forEach(s -> { s.close(); });
	}

	@Override
	public List<String> loginHttpSession(String sessionId, String protocol, String userName, String password) throws LoginException {
		logger.info("loginHttpSession: " + sessionId + ": " + userName);
		List<String> roles = ucService.verifyCredentials(userName, password);
		logger.info("loginHttpSession: OK");
		UserInfo ui = new UserInfo(sessionId, userName, protocol, roles);
		httpSessions.put(sessionId, ui);
		publishUserInfoChange();
		return roles;
	}

	@Override
	public boolean isValidHttpSession(String sessionId) {
		boolean result = (httpSessions.get(sessionId) != null);
		logger.fine("isValidHttpSession: " + sessionId + " = " + result);
		return result;
	}

	@Override
	public boolean isValidWsSession(String sessionId) {
		boolean result = (sessions.get(sessionId) != null);
		logger.fine("isValidWsSession: " + sessionId + " = " + result);
		return result;
	}

	@Override
	public void logoutHttpSession(String httpSessionId) {
		logger.info("logoutHttpSession: " + httpSessionId);
		UserInfo ui = httpSessions.remove(httpSessionId);
		if (ui == null) {
			logger.warning("unknown httpSessionId: " + httpSessionId);
		}
		List<String> wsSessions = wsSessionsForHttpSession.remove(httpSessionId);
		if (wsSessions != null) {
			wsSessions.forEach(s -> { 
				removeWsSessionId(s,wsSessionsForTopic); 
				WSSessionInfo wsSessionInfo = sessions.remove(s);
				if (wsSessionInfo != null) { 
					wsSessionInfo.close();
				}
				});
		} else {
			logger.warning("no such httpSessionId: " + httpSessionId);
		}
		publishUserInfoChange();
	}

	@Override
	public void changeHttpSessionId(String oldSessionId, String newSessionId) {
		logger.info("changeHttpSessionId: " + oldSessionId + " -> " + newSessionId);
		UserInfo ui = httpSessions.remove(oldSessionId);
		if (ui != null) {
			httpSessions.put(newSessionId, ui);
		}
		List<String> wsSessions = wsSessionsForHttpSession.remove(oldSessionId);
		if (wsSessions != null) {
			wsSessionsForHttpSession.put(newSessionId, wsSessions);
		}
		publishUserInfoChange();
	}

	@Override
	public UserInfo getUserHttpSessionInfo(String httpSessionId) throws LoginException {
		logger.info("getUserHttpSessionInfo: " + httpSessionId);
		UserInfo ui = httpSessions.get(httpSessionId);
		if (ui != null) {
			return new UserInfo(ui.getHttpSessionId(), ui.getUserName(), ui.getProtocol(), new ArrayList<>(ui.getRoles()));
		}
		throw new LoginException("no roles for httpSessionId: " + httpSessionId);
	}

	@Override
	public UserWsInfo getUserWsSessionInfo(String wsSessionId) throws LoginException {
		logger.info("getUserWsSessionInfo: " + wsSessionId);
		WSSessionInfo wsInfo = sessions.get(wsSessionId);
		if (wsInfo != null) {
			return new UserWsInfo(wsSessionId, wsInfo.getUserName(), wsInfo.getProtocol(), null); 
		}
		throw new LoginException("no roles for wsSessionId: " + wsSessionId);
	}

	@Override
	public List<String> loginWsSession(Session wsSession, String protocol, String userName, String password) throws LoginException {
		logger.info("loginWsSession standalone");
		List<String> roles = ucService.verifyCredentials(userName, password);
		sessions.put(wsSession.getId(), new WSSessionInfo(userName, wsSession, protocol, roles));
		publishUserInfoChange();
		return roles;
	}

	@Override
	public List<String> loginWsSession(Session wsSession, String httpSessionId, String protocol, String userName, String password)
			throws LoginException {
		logger.info("loginWsSession with associated http session");
		if (isValidHttpSession(httpSessionId)) {
			List<String> roles = ucService.verifyCredentials(userName, password);
			sessions.put(wsSession.getId(), new WSSessionInfo(userName, wsSession, protocol, roles));
			List<String> wsSessions = wsSessionsForHttpSession.get(httpSessionId);
			if (wsSessions == null) {
				wsSessions = Collections.synchronizedList(new ArrayList<String>());
				wsSessionsForHttpSession.put(httpSessionId, wsSessions);
			}
			wsSessions.add(wsSession.getId());
			publishUserInfoChange();
			return roles;
		}
	    throw new LoginException();
	}

	@Override
	public void addWsSession(Session wsSession, String httpSessionId, String protocol) throws LoginException {
		logger.info("addWsSession and associate it with http session");
		if (isValidHttpSession(httpSessionId)) {
			UserInfo ui = httpSessions.get(httpSessionId);
			sessions.put(wsSession.getId(), new WSSessionInfo(ui.getUserName(), wsSession, protocol, ui.getRoles()));
			List<String> wsSessions = wsSessionsForHttpSession.get(httpSessionId);
			if (wsSessions == null) {
				wsSessions = Collections.synchronizedList(new ArrayList<String>());
				wsSessionsForHttpSession.put(httpSessionId, wsSessions);
			}
			wsSessions.add(wsSession.getId());
			publishUserInfoChange();
		} else {
			throw new LoginException();
		}
	}

	@Override
	public void removeWsSession(String wsSessionId, boolean closeOnRemove) {
		logger.info("removeWsSession");
		removeWsSessionId(wsSessionId, wsSessionsForHttpSession);
		removeWsSessionId(wsSessionId, wsSessionsForTopic);
		WSSessionInfo wsSessionInfo = sessions.remove(wsSessionId);
		if (closeOnRemove) {
			wsSessionInfo.close();
		}
		publishUserInfoChange();
	}

	@Override
	public void subscribe(String wsSessionId, String topicId) {
		List<String> wsSessions = wsSessionsForTopic.get(topicId);
		if (wsSessions == null) {
			wsSessions = Collections.synchronizedList(new ArrayList<String>());
			wsSessionsForTopic.put(topicId, wsSessions);
			wsSessions.add(wsSessionId);
			publishUserInfoChange();
		} else {
			if (!wsSessions.contains(wsSessionId)) {
				wsSessions.add(wsSessionId);
				publishUserInfoChange();
			}
		}
	}

	@Override
	public void unSubscribe(String wsSessionId, String topicId) {
		List<String> wsSessions = wsSessionsForTopic.get(topicId);
		if (wsSessions != null) {
			wsSessions.remove(wsSessionId);
			if (wsSessions.size() == 0) {
				wsSessionsForTopic.remove(topicId);
			}
		}
		publishUserInfoChange();
	}

	@Override
	public int publishToTopic(String topicId, Message message) {
		int published = publishToSessionGroup(topicId, wsSessionsForTopic, sessions, message);
		logger.info("publishToTopic: " + topicId + " " + published);
		return published;
	}

	@Override
	public int publishToHttpSession(String httpSessionId, Message message) {
		int published = publishToSessionGroup(httpSessionId, wsSessionsForHttpSession, sessions, message);
		logger.info("publishToHttpSession: " + httpSessionId + " " + published);
		return published;
	}

	@Override
	public int publishToWsSession(String wsSessionId, Message message) {
		WSSessionInfo wsSession = sessions.get(wsSessionId);
		try {
			if (wsSession != null) {
				if (wsSession.usesBinaryProtocol()) {
					ByteArrayOutputStream entityOutputStream = new ByteArrayOutputStream();
					message.writeTo(entityOutputStream);
					ByteBuffer bb = ByteBuffer.wrap(entityOutputStream.toByteArray());
					wsSession.sendBinary(bb);
				} else {
					String jsonMessage = JsonFormat.printer().includingDefaultValueFields().print(message);
					wsSession.sendText(jsonMessage);
				}
				logger.fine("publishToWsSession: " + wsSessionId + " OK");
				return 1;
			}
		} catch (IOException e) {
			logger.severe("publishToWsSession IOException");
		}
		logger.severe("publishToWsSession: " + wsSessionId + " FAILED");
		return 0;
	}

	@Override
	public int publishToAll(Message message) {
		sessions.forEach((k,v) -> { v.sendMessage(message); });
		int published = sessions.size();
		logger.info("publishToAll: " + published);
		return published;
	}

	private void removeWsSessionId(String wsSessionId, Map<String, List<String>> treeList) {
		List<String> toRemove = new ArrayList<>();
		treeList.forEach((k,v) -> {
			if (v != null) {
				v.remove(wsSessionId);
				if (v.size() == 0) {
					toRemove.add(k);
				}
			}
		});
		toRemove.forEach(k -> { treeList.remove(k); });
	}
	
	private int publishToSessionGroup(String groupId, Map<String, List<String>> treeList, Map<String, WSSessionInfo> sessions, Message message) {
		List<String> wsSessions = treeList.get(groupId);
		if (wsSessions != null) {
			wsSessions.forEach(s -> { 
				WSSessionInfo session = sessions.get(s);
				if (session != null) {
					session.sendMessage(message); 
				}
			});
			return wsSessions.size();
		}
		return 0;
	}

	@Override
	public UserInfoData getUserData() {
		logger.info("getUserData");
		UserInfoData.Builder ulb = UserInfoData.newBuilder();
		httpSessions.values().forEach(u -> {
			HttpSessionInfo hsi = HttpSessionInfo.newBuilder()
					.setUserName(u.getUserName())
					.setHttpSessionId(u.getHttpSessionId())
					.setProtocol(u.getProtocol())
					.build();
			ulb.addHttpSessions(hsi);
		});
		sessions.values().forEach(s -> {
			WsSessionInfo wss = WsSessionInfo.newBuilder()
					.setUserName(s.getUserName())
					.setProtocol(s.getProtocol())
					.setWsSessionId(s.getSession().getId())
					.build();
			ulb.addWsSessions(wss);
		});
		wsSessionsForHttpSession.forEach((k,v) -> {
			HttpSessionWsSessionsInfo.Builder hswsiBuilder = HttpSessionWsSessionsInfo.newBuilder()
					.setHttpSessionId(k);
			v.forEach(w -> {
				hswsiBuilder.addWsSessionId(w);
			});
			ulb.addHttpSessionWsSessions(hswsiBuilder.build());
		});
		wsSessionsForTopic.forEach((k,v) -> {
			TopicInfo.Builder tib = TopicInfo.newBuilder()
					.setTopicId(k);
			v.forEach(w -> { 
				tib.addWsSessionId(w);
			});
			ulb.addTopics(tib.build());
		});
		return ulb.build();
	}
	
	private void publishUserInfoChange() {
		logger.info("publishUserInfoChange");
		WrapperMessage wm = WrapperMessage.newBuilder()
				.setUserInfoData(getUserData())
				.build();
		publishToTopic(USERINFO_TOPICID, wm);
	}

}
