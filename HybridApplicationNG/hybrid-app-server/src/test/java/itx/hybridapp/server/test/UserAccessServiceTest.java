package itx.hybridapp.server.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.Session;

import org.testng.Assert;
import org.testng.annotations.Test;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.protocols.UserAccessProtocol.HttpSessionWsSessionsInfo;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.UserInfoData;
import itx.hybridapp.server.services.useraccess.MessagePublisher;
import itx.hybridapp.server.services.useraccess.UserAccessService;
import itx.hybridapp.server.services.useraccess.UserAccessServiceImpl;

public class UserAccessServiceTest extends BaseTest {
	
	private static final Logger logger = Logger.getLogger(UserAccessServiceTest.class.getName());
	
	@Test
	public void testLoginLogoutHttpSession() {
		try {
			logger.info("testLoginLogoutHttpSession");
			String httpSessionId = "session1";
			UserAccessService uaService = createUserAccessService();
			Assert.assertFalse(uaService.isValidHttpSession("session1"));
			uaService.loginHttpSession(httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession("session1"));
			uaService.logoutHttpSession(httpSessionId);
			Assert.assertFalse(uaService.isValidHttpSession(httpSessionId));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}

	@Test
	public void testLoginLogoutWsSession() {
		try {
			logger.info("testLoginLogoutWsSession");
			String wsSessionId = "wssession1";
			UserAccessService uaService = createUserAccessService();
			Session wsSession = createSession(wsSessionId);
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
			uaService.loginWsSession(wsSession, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidWsSession(wsSessionId));
			uaService.removeWsSession(wsSession.getId(), true);
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}

	@Test
	public void loginLogoutUseAssociatedWSTest() {
		try {
			logger.info("loginLogoutUseAssociatedWSTest");
			String httpSessionId = "session1";
			String wsSessionId = "wssession1";
			UserAccessServiceImpl uaService = createUserAccessService();
			Session wsSession = createSession(wsSessionId);
			uaService.loginHttpSession(httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
			uaService.addWsSession(wsSession, httpSessionId, ProtoMediaType.APPLICATION_JSON);
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertTrue(uaService.isValidWsSession(wsSessionId));
			
			UserInfoData uiData = uaService.getUserData();
			Assert.assertNotNull(uiData);
			List<HttpSessionWsSessionsInfo> sessionRelationList = uiData.getHttpSessionWsSessionsList();
			Assert.assertNotNull(sessionRelationList);
			Assert.assertTrue(sessionRelationList.size() == 1);
			Assert.assertEquals(sessionRelationList.get(0).getHttpSessionId(), httpSessionId);
			Assert.assertTrue(sessionRelationList.get(0).getWsSessionIdCount() == 1);
			Assert.assertEquals(sessionRelationList.get(0).getWsSessionId(0), wsSessionId);
			
			uaService.logoutHttpSession(httpSessionId);
			Assert.assertFalse(uaService.isValidHttpSession(httpSessionId));
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}

	@Test
	public void loginLogoutUseAssociatedWSLoginTest() {
		try {
			logger.info("loginLogoutUseAssociatedWSLoginTest");
			String httpSessionId = "session1";
			String wsSessionId = "wssession1";
			UserAccessService uaService = createUserAccessService();
			Session wsSession = createSession(wsSessionId);
			uaService.loginHttpSession(httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
			uaService.loginWsSession(wsSession, httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertTrue(uaService.isValidWsSession(wsSessionId));
			uaService.logoutHttpSession(httpSessionId);
			Assert.assertFalse(uaService.isValidHttpSession(httpSessionId));
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}

	@Test
	public void loginLogoutUseIndependentWSLoginTest() {
		try {
			logger.info("loginLogoutUseIndependentWSLoginTest");
			String httpSessionId = "session1";
			String wsSessionId = "wssession1";
			UserAccessService uaService = createUserAccessService();
			Session wsSession = createSession(wsSessionId);
			uaService.loginHttpSession(httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
			uaService.loginWsSession(wsSession, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertTrue(uaService.isValidWsSession(wsSessionId));
			uaService.logoutHttpSession(httpSessionId);
			Assert.assertFalse(uaService.isValidHttpSession(httpSessionId));
			Assert.assertTrue(uaService.isValidWsSession(wsSessionId));
			uaService.removeWsSession(wsSessionId, true);
			Assert.assertFalse(uaService.isValidHttpSession(httpSessionId));
			Assert.assertFalse(uaService.isValidWsSession(wsSessionId));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}
	
	@Test
	public void publishSubscribeTest() {
		try {
			logger.info("publishSubscribeTest");
			String httpSessionId = "session1";
			String wsSessionId = "wssession1";
			String topicId = "/test";
			LoginRequest message = LoginRequest.newBuilder().setUserName("user").setPassword("password").build();
			
			int sendMessages;
			UserAccessServiceImpl uaServiceImpl = createUserAccessService();
			UserAccessService uaService = uaServiceImpl;
			MessagePublisher messagePublisher = uaServiceImpl;

			sendMessages = messagePublisher.publishToWsSession(wsSessionId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToTopic(topicId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToHttpSession(httpSessionId, message);
			Assert.assertTrue(sendMessages == 0);

			Session wsSession = createSession(wsSessionId);
			uaService.loginHttpSession(httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			uaService.loginWsSession(wsSession, httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Assert.assertTrue(uaService.isValidHttpSession(httpSessionId));
			Assert.assertTrue(uaService.isValidWsSession(wsSessionId));
			
			sendMessages = messagePublisher.publishToWsSession("notexistingSessionId", message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToTopic(topicId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToHttpSession(httpSessionId, message);
			Assert.assertTrue(sendMessages == 1);
			
			uaService.subscribe(wsSessionId, topicId);
			
			sendMessages = messagePublisher.publishToWsSession(wsSessionId, message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToTopic(topicId, message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToHttpSession(httpSessionId, message);
			Assert.assertTrue(sendMessages == 1);
			
			uaService.unSubscribe(wsSessionId, topicId);
			
			sendMessages = messagePublisher.publishToWsSession(wsSessionId, message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 1);
			sendMessages = messagePublisher.publishToHttpSession(httpSessionId, message);
			Assert.assertTrue(sendMessages == 1);

			uaService.removeWsSession(wsSessionId, true);
			
			sendMessages = messagePublisher.publishToWsSession(wsSessionId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToTopic(topicId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToHttpSession(httpSessionId, message);
			Assert.assertTrue(sendMessages == 0);

			uaService.logoutHttpSession(httpSessionId);
			
			sendMessages = messagePublisher.publishToWsSession(wsSessionId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToTopic(topicId, message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToAll(message);
			Assert.assertTrue(sendMessages == 0);
			sendMessages = messagePublisher.publishToHttpSession(httpSessionId, message);
			Assert.assertTrue(sendMessages == 0);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}

}
