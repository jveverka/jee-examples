package ite.examples.test;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import ite.examples.ws.dto.AuthResponse;
import ite.examples.ws.dto.InitialData;
import ite.examples.ws.dto.LogOnRequest;
import ite.examples.ws.dto.UpdateData;
import ite.examples.ws.dto.UpdateRequest;

public class UnitTests {
	
	private static final String BaseURL = "http://localhost:8080/jsf-08-lab/ws";
	private static final String GetSessionIdURL = BaseURL + "/userws/getSessionId";
	private static final String LogOnURL = BaseURL + "/userws/logOn";
	private static final String LogOffURL = BaseURL + "/userws/logOff";
	private static final String GetInitialDataURL = BaseURL + "/dataws/getInitalData";
	private static final String UpdateDataURL = BaseURL + "/dataws/updateData";

	@Test
	public void simpleLogonLogoffTest() {
		Gson gson = new Gson();
		
		String responseData = HTTPUtils.callPOST(GetSessionIdURL, "", null);
		AuthResponse response = gson.fromJson(responseData, AuthResponse.class);
        String sessionId = response.getResponse();
        
        //send login request
		LogOnRequest logonRequest = new LogOnRequest("testUser1", "testPassword1");
		String logonData = gson.toJson(logonRequest);
		responseData = HTTPUtils.callPOST(LogOnURL, logonData, sessionId);
		response = gson.fromJson(responseData, AuthResponse.class);
		System.out.println("response: " + response.getResponse());
		if ("OK".equals(response.getResponse())) {
			//send logoff request 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			responseData = HTTPUtils.callPOST(LogOffURL, "", sessionId);
			System.out.println(responseData);
			response = gson.fromJson(responseData, AuthResponse.class);
			Assert.assertNotNull(response, "expected response != null");
			Assert.assertEquals("OK", response.getResponse(), "Expected return code for logoff is OK");
		} else {
			Assert.fail("Login failed");
		}
	}

	@Test
	public void updateDataTest() {
		Gson gson = new Gson();
		
		String responseData = HTTPUtils.callPOST(GetSessionIdURL, "", null);
		AuthResponse response = gson.fromJson(responseData, AuthResponse.class);
        String sessionId = response.getResponse();
        
        //send login request
		LogOnRequest logonRequest = new LogOnRequest("testUser1", "testPassword1");
		String logonData = gson.toJson(logonRequest);
		responseData = HTTPUtils.callPOST(LogOnURL, logonData, sessionId);
		response = gson.fromJson(responseData, AuthResponse.class);
		System.out.println("response: " + response.getResponse());
		if ("OK".equals(response.getResponse())) {
			//get initial data
			responseData = HTTPUtils.callPOST(GetInitialDataURL, "", sessionId);
			InitialData initialData = gson.fromJson(responseData, InitialData.class);
			Assert.assertNotNull(initialData, "expected initialData != null");
			Assert.assertEquals("OK", initialData.getReturnCode(), "expected return code OK !");
			Assert.assertNotNull(initialData.getData(), "expected table data != null");
			Assert.assertTrue(initialData.getData().size() == 4, "4 rows expected");
			
			for (List<String> row: initialData.getData()) {
				Assert.assertNotNull(row, "row data exoected");
				Assert.assertTrue(row.size() == 4, "4 columns expected");
			}
			
			//update data
			String data = "yy";
			UpdateRequest updateRequest = new UpdateRequest(1,1,data);
			String updateData = gson.toJson(updateRequest);
			responseData = HTTPUtils.callPOST(UpdateDataURL, updateData, sessionId);
			UpdateData updateResult = gson.fromJson(responseData, UpdateData.class);
			Assert.assertNotNull(updateResult, "expected updateResult != null");
			Assert.assertEquals("OK", updateResult.getReturnCode(), "expected return code OK !");

			//get initial data again
			responseData = HTTPUtils.callPOST(GetInitialDataURL, "", sessionId);
			initialData = gson.fromJson(responseData, InitialData.class);
			Assert.assertNotNull(initialData, "expected initialData != null");
			Assert.assertEquals("OK", initialData.getReturnCode(), "expected return code OK !");
			String expectedData = initialData.getData().get(1).get(1);
			Assert.assertEquals(data, expectedData, "expected same data !");

			//log off
			responseData = HTTPUtils.callPOST(LogOffURL, "", sessionId);
			response = gson.fromJson(responseData, AuthResponse.class);
			Assert.assertNotNull(response, "expected response != null");
			Assert.assertEquals("OK", response.getResponse(), "Expected return code for logoff is OK");
		} else {
			Assert.fail("Login failed");
		}
	}

}
