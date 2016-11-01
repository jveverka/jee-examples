package itx.hybridapp.common.client.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.client.ClientUtils;
import itx.hybridapp.common.client.test.websockets.ScenarioStep;
import itx.hybridapp.common.client.test.websockets.WSSessionScenario;
import itx.hybridapp.common.client.test.websockets.WSTestListener;
import itx.hybridapp.common.client.websocket.WSClient;


public class UserAccessWSTest extends UserAccessTest {

	/**
	 * test successful (using valid credentials) application login using websocket endpoint
	 * @param userName
	 * @param password
	 * @param expectedRoles
	 * @param mediaType
	 */
	@Test(dataProvider = "loginLogoutProvider")
	public void testWsEndpointLoginLogout(String userName, String password, String[] expectedRoles, String mediaType) {
		try {
			List<ScenarioStep> steps = new ArrayList<>();
			ScenarioStep step = TestUtils.createLoginTestStep(userName, password, expectedRoles);
			steps.add(step);
			WSSessionScenario scenario = new WSSessionScenario(steps, ProtoMediaType.isBinaryProtocol(mediaType));
			WSTestListener testListener = new WSTestListener(scenario);

			WSClient wsClient = WSClient.buildClient(TestUtils.WSBASE_URL + ClientUtils.WSENDPOINT_URL, mediaType, testListener);
			wsClient.startClientBlocking();
			TestUtils.evaluateScenario(scenario);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * test unsuccessful (using invalid credentials) application login using websocket endpoint
	 * @param userName
	 * @param password
	 * @param mediaType
	 */
	@Test(dataProvider = "invalidLoginProvider")
	public void testWsEndpointInvalidLogin(String userName, String password, String mediaType) {
		try {
			List<ScenarioStep> steps = new ArrayList<>();
			ScenarioStep step = TestUtils.createLoginTestStep(userName, password, null);
			steps.add(step);
			WSSessionScenario scenario = new WSSessionScenario(steps, ProtoMediaType.isBinaryProtocol(mediaType));
			WSTestListener testListener = new WSTestListener(scenario);

			WSClient wsClient = WSClient.buildClient(TestUtils.WSBASE_URL + ClientUtils.WSENDPOINT_URL, mediaType, testListener);
			wsClient.startClientBlocking();
			TestUtils.evaluateScenario(scenario);
		} catch (Exception e) {
			Assert.fail();
		}
	}

}
