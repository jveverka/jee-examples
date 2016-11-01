package itx.hybridapp.common.client.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.Status;

import org.testng.Assert;
import org.testng.annotations.Test;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.client.ClientUtils;
import itx.hybridapp.common.client.rest.DataServiceClient;
import itx.hybridapp.common.client.rest.UserAccessClient;
import itx.hybridapp.common.client.test.websockets.ScenarioStep;
import itx.hybridapp.common.client.test.websockets.WSSessionScenario;
import itx.hybridapp.common.client.test.websockets.WSTestListener;
import itx.hybridapp.common.client.websocket.WSClient;

public class UserAccessMixedTest extends UserAccessTest {
	
	private static final Logger logger = Logger.getLogger(UserAccessMixedTest.class.getName());
	
	@Test(dataProvider = "loginLogoutProvider")
	public void loginLogoutUseWSTest(String userName, String password, String[] expectedRoles, String mediaType) {
		try {
		logger.info("loginLogoutUseWSTest ...");
		UserAccessClient uaClient = UserAccessClient.buildClient(TestUtils.BASE_URL, mediaType);
		DataServiceClient dsClient = DataServiceClient.buildClient(TestUtils.BASE_URL, mediaType);

		List<String> roles = uaClient.login(userName, password);
		Assert.assertTrue(TestUtils.compareRoles(roles, expectedRoles));
		Assert.assertTrue(uaClient.isValidSession());

		dsClient.setHttpSessionId(uaClient.getHttpSessionId());
		String resultData = dsClient.getData(TestUtils.TEST_DATA);
		Assert.assertEquals(resultData, (TestUtils.TEST_RESULT_PREFIX + TestUtils.TEST_DATA));

		List<ScenarioStep> steps = new ArrayList<>();
		steps.add(TestUtils.createLoginTestStep(userName, password, expectedRoles));
		steps.add(TestUtils.createDataServiceTeststep(TestUtils.TEST_DATA, true));
		WSSessionScenario scenario = new WSSessionScenario(steps, ProtoMediaType.isBinaryProtocol(mediaType));
		WSTestListener testListener = new WSTestListener(scenario);

		WSClient wsClient = WSClient.buildClient(TestUtils.WSBASE_URL + ClientUtils.WSENDPOINT_URL, mediaType, testListener);
		wsClient.startClientBlocking();
		TestUtils.evaluateScenario(scenario);

		uaClient.logout();
		
		logger.info("loginLogoutUseWSTest done.");
		} catch (Exception e) {
			Assert.fail();
		}
	}

}
