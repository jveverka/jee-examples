package itx.hybridapp.common.client.test;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import itx.hybridapp.common.client.test.websockets.ScenarioStep;
import itx.hybridapp.common.client.test.websockets.WSSessionScenario;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestRequest;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestResponse;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginResponse;

public final class TestUtils {
	
	public static final String BASE_URL = "http://localhost:8080/hybridapp/ws"; 
	public static final String WSBASE_URL = "ws://localhost:8080/hybridapp"; 

	public static final String TEST_DATA = "data";
	public static final String TEST_RESULT_PREFIX = "response:";

	public static boolean compareRoles(List<String> roles, String[] expectedRoles) {
		try {
			if (roles.size() == expectedRoles.length) {
				for (int i=0; i<roles.size(); i++) {
					if (!roles.get(i).equals(expectedRoles[i])) {
						return false;
					}
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void evaluateScenario(WSSessionScenario scenario) {
		for (ScenarioStep step: scenario.getSteps()) {
			Assert.assertTrue(step.isWasExecuted());
			if (step.isExpectedException()) {
				Assert.assertNotNull(step.getActualException());
			} else {
				Assert.assertNull(step.getActualException());
			}
			if (step.getExpectedResponse() != null) {
				Assert.assertTrue(evaluateTestStep(step));
			} else {
				Assert.assertNull(step.getActualResponse());
			}
		}
	}
	
	private static boolean evaluateTestStep(ScenarioStep step) {
		if (step.getExpectedResponse().getMsgCase().getNumber() == WrapperMessage.LOGINRESPONSE_FIELD_NUMBER) {
			return step.getExpectedResponse().getLoginResponse().getUserName().equals(step.getActualResponse().getLoginResponse().getUserName());
		} else {
			return step.getExpectedResponse().equals(step.getActualResponse());
		}
	}
	
	public static ScenarioStep createLoginTestStep(String userName, String password, String[] expectedRoles) {
		WrapperMessage expectedResponse = null;
		LoginRequest loginRequest = LoginRequest.newBuilder().setUserName(userName).setPassword(password).build();
		WrapperMessage loginRequestWrapper = WrapperMessage.newBuilder().setLoginRequest(loginRequest).build();
		if (expectedRoles != null) {
			LoginResponse expectedLoginResponse = LoginResponse.newBuilder().setUserName(userName).addAllRole(Arrays.asList(expectedRoles)).build();
			expectedResponse = WrapperMessage.newBuilder().setLoginResponse(expectedLoginResponse).build();
		}
		return new ScenarioStep(loginRequestWrapper, expectedResponse, false);
	}

	public static ScenarioStep createDataServiceTeststep(String data, boolean expectedOk) {
		WrapperMessage expectedResponse = null;
		TestRequest testRequest = TestRequest.newBuilder().setData(data).build();
		WrapperMessage testRequestwrapper = WrapperMessage.newBuilder().setTestRequest(testRequest).build();
		if (expectedOk) {
			TestResponse testResponse = TestResponse.newBuilder().setData(TEST_RESULT_PREFIX + data).build();
			expectedResponse = WrapperMessage.newBuilder().setTestResponse(testResponse).build();
		}
		return new ScenarioStep(testRequestwrapper, expectedResponse, false);
	}

}
