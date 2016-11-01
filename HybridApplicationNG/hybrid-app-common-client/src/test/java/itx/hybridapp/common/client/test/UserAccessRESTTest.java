package itx.hybridapp.common.client.test;

import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import org.testng.Assert;
import org.testng.annotations.Test;

import itx.hybridapp.common.client.ClientServiceException;
import itx.hybridapp.common.client.rest.DataServiceClient;
import itx.hybridapp.common.client.rest.UserAccessClient;

public class UserAccessRESTTest extends UserAccessTest {
	
	private static final Logger logger = Logger.getLogger(UserAccessRESTTest.class.getName());
	
	/**
	 * login and logout using REST APIs and JSON or Protobuffer protocol
	 * this test uses valid user credentials
	 * @param userName
	 * @param password
	 * @param expectedRoles
	 * @param mediaType
	 */
	@Test(dataProvider = "loginLogoutProvider")
	public void loginLogoutTest(String userName, String password, String[] expectedRoles, String mediaType) {
		logger.info("loginLogoutTest ...");
		try {
			UserAccessClient uaClient = UserAccessClient.buildClient(TestUtils.BASE_URL, mediaType);
			DataServiceClient dsClient = DataServiceClient.buildClient(TestUtils.BASE_URL, mediaType);
			try {
				dsClient.getData(TestUtils.TEST_DATA);
				Assert.fail();
			} catch (ClientServiceException e) {
				logger.info("expected ClientServiceException");
			}
			Assert.assertFalse(uaClient.isValidSession());
			List<String> roles = uaClient.login(userName, password);
			Assert.assertTrue(TestUtils.compareRoles(roles, expectedRoles));
			Assert.assertTrue(uaClient.isValidSession());
			dsClient.setHttpSessionId(uaClient.getHttpSessionId());
			String resultData = dsClient.getData(TestUtils.TEST_DATA);
			Assert.assertEquals(resultData, (TestUtils.TEST_RESULT_PREFIX + TestUtils.TEST_DATA));
			uaClient.logout();
			try {
				dsClient.getData(TestUtils.TEST_DATA);
				Assert.fail();
			} catch (ClientServiceException e) {
				logger.info("expected ClientServiceException");
			}
			Assert.assertFalse(uaClient.isValidSession());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * login using REST APIs and JSON or Protobuffer protocol
	 * this test uses invalid user credentials, so login fails
	 * @param userName
	 * @param password
	 * @param mediaType
	 */
	@Test(dataProvider = "invalidLoginProvider")
	public void invalidLoginTest(String userName, String password, String mediaType) {
		try {
			UserAccessClient uaClient = UserAccessClient.buildClient(TestUtils.BASE_URL, mediaType);
			try {
				logger.info("invalidLoginTest ...");
				Assert.assertFalse(uaClient.isValidSession());
				uaClient.login(userName, password);
				Assert.fail();
			} catch (LoginException e) {
				logger.info("expected LoginException");
			}
			Assert.assertFalse(uaClient.isValidSession());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
}
