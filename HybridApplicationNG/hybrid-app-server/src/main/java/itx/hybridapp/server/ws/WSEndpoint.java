package itx.hybridapp.server.ws;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import itx.hybridapp.common.protocols.UserAccessProtocol.LoginRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginResponse;
import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WsSessionPublishWithReplyMessage;
import itx.hybridapp.server.services.TopicPublisher;
import itx.hybridapp.server.services.dto.UserInfo;
import itx.hybridapp.server.services.dto.WsSessionDestroyedMessage;
import itx.hybridapp.server.services.dto.WsSessionMessageWrapper;
import itx.hybridapp.server.services.test.TestJobManager;
import itx.hybridapp.server.services.useraccess.MessagePublisher;
import itx.hybridapp.server.services.useraccess.UserAccessService;
import itx.hybridapp.server.utils.WSUtils;

@ServerEndpoint("/ws/wsendpoint")
public class WSEndpoint {
	
	private final static Logger logger = Logger.getLogger(WSEndpoint.class.getName());
	
	@Inject
	private UserAccessService uaService;
	
	@Inject 
	private MessagePublisher messagePublisher;

	@Inject
	private TopicPublisher topicPublisher;
	
	@Inject
	private TestJobManager testJobManager;

	@OnOpen
	public void onOpen(Session session) {
		logger.info("onOpen");
		try {
			String httpSessionId = session.getRequestParameterMap().get("httpSessionData").get(0);
			String contentType = session.getRequestParameterMap().get("httpSessionData").get(1);
			logger.info("onOpen WS: httpSessionId: " + httpSessionId + " " + contentType);
			if (uaService.isValidHttpSession(httpSessionId)) {
				contentType = ProtoMediaType.getProtocolOrDefault(contentType);
				uaService.addWsSession(session, httpSessionId, contentType);
				UserInfo ui = uaService.getUserHttpSessionInfo(httpSessionId);
				LoginResponse loginResponse = LoginResponse.newBuilder()
						.setUserName(ui.getUserName())
						.setWsSessionId(session.getId())
						.addAllRole(ui.getRoles())
						.build(); 
				WrapperMessage wmResponse = WrapperMessage.newBuilder().setLoginResponse(loginResponse).build();
				WSUtils.sendMessage(session, wmResponse, ProtoMediaType.isBinaryProtocol(contentType));				
				logger.info("httpSession is already authorized");
			} else {
				logger.info("onOpen WS: httpSessionId is not authorized");
			}
		} catch (LoginException e) {
			logger.info("automatic login for httpSession has failed");
		} catch (Exception e) {
			logger.info("failed to get httpSessionId");
		}
	}
	
	/**
	 * handle JSON messages
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		try {
			WrapperMessage.Builder builder = WrapperMessage.newBuilder();
			JsonFormat.parser().merge(message, builder);
			WrapperMessage wm = builder.build();
			processMessage(session, wm, false);
		} catch (InvalidProtocolBufferException e) {
			logger.log(Level.SEVERE, "onMessage: ", e);
		}
	}
	
	/**
	 * handle binary messages
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void onMessage(byte[] message, Session session) {
		try {
			WrapperMessage wm = WrapperMessage.parseFrom(message);
			processMessage(session, wm, true);
		} catch (InvalidProtocolBufferException e) {
			logger.log(Level.SEVERE, "onMessage: ", e);
		}
	}
	 
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("onClose: " + session.getId());
		uaService.removeWsSession(session.getId(), false);
		topicPublisher.publish(new WsSessionDestroyedMessage(session.getId()));
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("onError");
		topicPublisher.publish(new WsSessionDestroyedMessage(session.getId()));
	}

	private void processMessage(Session session, WrapperMessage wm, boolean isBinaryMessage) {
		int messageTypeId = wm.getMsgCase().getNumber();
		try {
		if (uaService.isValidWsSession(session.getId())) {
			logger.fine("processing message: " + messageTypeId);
			switch (messageTypeId) {
			case WrapperMessage.ECHODATA_FIELD_NUMBER: {
				testJobManager.onTestJobReply(wm.getEchoData());
			    }
			    break;
			case WrapperMessage.TOPICSUBSCRIBE_FIELD_NUMBER: {
				String topicId = wm.getTopicSubscribe().getTopicId();
				logger.info("Topic subscribe: " + session.getId() + ":" + topicId);
				uaService.subscribe(session.getId(), topicId);
				}
				break;
			case WrapperMessage.TOPICUNSUBSCRIBE_FIELD_NUMBER: {
				String topicId = wm.getTopicUnsubscribe().getTopicId();
				logger.info("Topic unsubscribe: " + session.getId() + ":" + topicId);
				uaService.unSubscribe(session.getId(), topicId);
				}
				break;
			case WrapperMessage.TOPICPUBLISHMESSAGE_FIELD_NUMBER: {
				String topicId = wm.getTopicPublishMessage().getTopicId();
				logger.info("Topic publish: " + topicId);
				messagePublisher.publishToTopic(topicId, wm.getTopicPublishMessage().getMessage());
			    }
			    break;
			case WrapperMessage.WSSESSIONPUBLISHMESSAGE_FIELD_NUMBER: {
				String wsSessionId = wm.getWsSessionPublishMessage().getWsSessionId();
				logger.info("Publish to wsSession: " + wsSessionId);
				messagePublisher.publishToWsSession(wsSessionId, wm.getWsSessionPublishMessage().getMessage());
			    }
			    break;
			case WrapperMessage.HTTPSESSIONPUBLISHMESSAGE_FIELD_NUMBER: {
				String httpSessionId = wm.getHttpSessionPublishMessage().getHttpSessionId();
				logger.info("Publish to httpSession: " + httpSessionId);
				messagePublisher.publishToHttpSession(httpSessionId, wm.getHttpSessionPublishMessage().getMessage());
			    }
			    break; 
			case WrapperMessage.WSSESSIONPUBLISHWITHREPLYMESSAGE_FIELD_NUMBER: {
				String wsSessionId = wm.getWsSessionPublishWithReplyMessage().getWsSessionId();
				String replyToWsSessionId = session.getId();
				WsSessionPublishWithReplyMessage msg = 
						WsSessionPublishWithReplyMessage.newBuilder()
						.setReplyToWsSessionId(replyToWsSessionId)
						.setMessage(wm.getWsSessionPublishWithReplyMessage().getMessage())
						.build();
				logger.info("Publish to wsSession with reply: " + wsSessionId + " replyto " + replyToWsSessionId);
				messagePublisher.publishToWsSession(wsSessionId, msg);
			    }
			    break;
			default:
				logger.info("unsupported message type: " + messageTypeId + " sending to JMS toppic for further processing ...");
				WsSessionMessageWrapper messageWrappeer = 
						new WsSessionMessageWrapper(session.getId(), wm, ProtoMediaType.getProtocol(isBinaryMessage));
				topicPublisher.publish(messageWrappeer);
			}
		} else if (WrapperMessage.LOGINREQUEST_FIELD_NUMBER == messageTypeId) {
			try {
				logger.info("WS: processing user login");
				LoginRequest loginRequest = wm.getLoginRequest();
				List<String> roles = null;
				if (loginRequest.getHttpSessionId() != null && loginRequest.getHttpSessionId().length() > 0) {
					roles = uaService.loginWsSession(session, loginRequest.getHttpSessionId(),
						loginRequest.getProtocol(), loginRequest.getUserName(), loginRequest.getPassword());
				} else {
					roles = uaService.loginWsSession(session, 
							loginRequest.getProtocol(), loginRequest.getUserName(), loginRequest.getPassword());
				}
				logger.info("WS: login OK");
				LoginResponse loginResponse = LoginResponse.newBuilder()
						.setUserName(loginRequest.getUserName())
						.setWsSessionId(session.getId())
						.addAllRole(roles)
						.build(); 
				WrapperMessage wmResponse = WrapperMessage.newBuilder().setLoginResponse(loginResponse).build();
				WSUtils.sendMessage(session, wmResponse, isBinaryMessage);
			} catch (LoginException e) {
				logger.info("WS: LoginException");
				closeSession(session);
			}
		} else {
			logger.info("invalid session, disconnecting ...");
			closeSession(session);
		}
		} catch (IOException e) {
			logger.info("WS: IOException");
			closeSession(session);
		}
	}
	
	private void closeSession(Session session) {
		logger.info("closeSession");
		try {
			session.close();
			topicPublisher.publish(new WsSessionDestroyedMessage(session.getId()));
		} catch (IOException e) {
			logger.severe("can't close web socket session");
		}
	}
	

}
