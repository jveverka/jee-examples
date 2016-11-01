package itx.hybridapp.common.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import itx.hybridapp.common.protocols.UserAccessProtocol.LoginRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginResponse;
import itx.hybridapp.common.providers.uaprotocol.LoginRequestJsonProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginRequestProtobufProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginResponseJsonProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginResponseProtobufProvider;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.ControlOutputEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;

public class ProtoBufferTests {
	
	private static final Logger logger = Logger.getLogger(ProtoBufferTests.class.getName());
	
	@Test
	public void testProtoProvider() {
		try {
			LoginRequestProtobufProvider loginRequestProtoProvider = new LoginRequestProtobufProvider();
			LoginResponseProtobufProvider loginResponseProtoProvider = new LoginResponseProtobufProvider();
			ByteArrayOutputStream entityOutputStream;
			InputStream entityInputStream;
			
			LoginRequest loginRequest = LoginRequest.newBuilder().setUserName("user").setPassword("password").build();
			LoginResponse loginResponse = LoginResponse.newBuilder().setUserName("user").addRole("user").build();

			//write and read login request
			entityOutputStream = new ByteArrayOutputStream();
			loginRequestProtoProvider.writeTo(loginRequest, LoginRequest.class, null, null, null, null, entityOutputStream);
			entityInputStream = new ByteArrayInputStream(entityOutputStream.toByteArray());
			LoginRequest transformedLoginRequest = (LoginRequest)loginRequestProtoProvider
					.readFrom(LoginRequest.class, null, null, null, null, entityInputStream);
			Assert.assertEquals(loginRequest, transformedLoginRequest);
			entityOutputStream.close();
			entityInputStream.close();

			//write and read login response
			entityOutputStream = new ByteArrayOutputStream();
			loginResponseProtoProvider.writeTo(loginResponse, LoginRequest.class, null, null, null, null, entityOutputStream);
			entityInputStream = new ByteArrayInputStream(entityOutputStream.toByteArray());
			LoginResponse transformedLoginResponse = (LoginResponse)loginResponseProtoProvider
					.readFrom(LoginResponse.class, null, null, null, null, entityInputStream);
			Assert.assertEquals(loginResponse, transformedLoginResponse);
			entityOutputStream.close();
			entityInputStream.close();

		} catch (WebApplicationException e) {
			logger.log(Level.SEVERE, "WebApplicationException", e);
			Assert.fail();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
			Assert.fail();
		}
	}

	@Test
	public void testJsonProvider() {
		try {
			LoginRequestJsonProvider loginRequestJsonProvider = new LoginRequestJsonProvider();
			LoginResponseJsonProvider loginResponseJsonProvider = new LoginResponseJsonProvider();
			ByteArrayOutputStream entityOutputStream;
			InputStream entityInputStream;
			byte[] rawData;
			String jsonString;
			JsonElement jsonElement;

			LoginRequest loginRequest = LoginRequest.newBuilder().setUserName("user").setPassword("password").build();
			LoginResponse loginResponse = LoginResponse.newBuilder().setUserName("user").addRole("user").build();

			// write and read login request
			entityOutputStream = new ByteArrayOutputStream();
			loginRequestJsonProvider.writeTo(loginRequest, LoginRequest.class, null, null, null, null, entityOutputStream);
			rawData = entityOutputStream.toByteArray();
			entityInputStream = new ByteArrayInputStream(rawData);
			// try to read json by gson
			jsonString = new String(rawData, Charsets.UTF_8);
			logger.info(jsonString);
			jsonElement = new JsonParser().parse(jsonString);
			Assert.assertEquals("user", jsonElement.getAsJsonObject().get("userName").getAsString());
			Assert.assertEquals("password", jsonElement.getAsJsonObject().get("password").getAsString());
			// read from bytearray to LoginRequest
			LoginRequest transformedLoginRequest = (LoginRequest) loginRequestJsonProvider.readFrom(LoginRequest.class, null, null,
					null, null, entityInputStream);
			Assert.assertEquals(loginRequest, transformedLoginRequest);
			entityOutputStream.close();
			entityInputStream.close();
			
			//write and read login response
			entityOutputStream = new ByteArrayOutputStream();
			loginResponseJsonProvider.writeTo(loginResponse, LoginRequest.class, null, null, null, null, entityOutputStream);
			rawData = entityOutputStream.toByteArray();
			// try to read json by gson
			jsonString = new String(rawData, Charsets.UTF_8);
			logger.info(jsonString);
			jsonElement = new JsonParser().parse(jsonString);
			Assert.assertEquals("user", jsonElement.getAsJsonObject().get("userName").getAsString());
			JsonArray jsonRoles = jsonElement.getAsJsonObject().get("role").getAsJsonArray();
			Assert.assertTrue(jsonRoles.size() == 1);
			Assert.assertEquals("user", jsonRoles.get(0).getAsString());
			// read from bytearray to LoginResponse
			entityInputStream = new ByteArrayInputStream(rawData);
			LoginResponse transformedLoginResponse = (LoginResponse)loginResponseJsonProvider
					.readFrom(LoginResponse.class, null, null, null, null, entityInputStream);
			Assert.assertEquals(loginResponse, transformedLoginResponse);
			entityOutputStream.close();
			entityInputStream.close();

		} catch (WebApplicationException e) {
			logger.log(Level.SEVERE, "WebApplicationException", e);
			Assert.fail();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
			Assert.fail();
		}
	}
	
	@Test
	public void testWrapper() {
		try {
			ByteArrayOutputStream entityOutputStream;
			byte[] rawData;
			WrapperMessage wm;
			int msgOrdinal;
			LoginRequest loginRequest = LoginRequest.newBuilder().setUserName("user").setPassword("password").build();
			LoginResponse loginResponse = LoginResponse.newBuilder().setUserName("user").addRole("user").build();
			
			wm = WrapperMessage.newBuilder().setLoginRequest(loginRequest).build();
			entityOutputStream = new ByteArrayOutputStream();
			wm.writeTo(entityOutputStream);
			rawData = entityOutputStream.toByteArray();
			wm = WrapperMessage.parseFrom(rawData);
			msgOrdinal = wm.getMsgCase().getNumber();
			Assert.assertTrue(msgOrdinal == WrapperMessage.LOGINREQUEST_FIELD_NUMBER);
			LoginRequest acquiredLoginRequest = wm.getLoginRequest();
			Assert.assertEquals(acquiredLoginRequest.getUserName(), loginRequest.getUserName());
			Assert.assertEquals(acquiredLoginRequest.getPassword(), loginRequest.getPassword());

			wm = WrapperMessage.newBuilder().setLoginResponse(loginResponse).build();
			entityOutputStream = new ByteArrayOutputStream();
			wm.writeTo(entityOutputStream);
			rawData = entityOutputStream.toByteArray();
			wm = WrapperMessage.parseFrom(rawData);
			msgOrdinal = wm.getMsgCase().getNumber();
			Assert.assertTrue(msgOrdinal == WrapperMessage.LOGINRESPONSE_FIELD_NUMBER);
			LoginResponse acquiredLoginResponse = wm.getLoginResponse();
			Assert.assertEquals(acquiredLoginResponse.getUserName(), loginResponse.getUserName());

		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
			Assert.fail();
		}
	}

	@Test
	public void testWrapperJsonStringSerialization() {

		try {
			WrapperMessage wm;
			LoginRequest loginRequest = LoginRequest.newBuilder().setUserName("user").setPassword("password").build();
			
			wm = WrapperMessage.newBuilder().setLoginRequest(loginRequest).build();
			String jsonMessage = JsonFormat.printer().includingDefaultValueFields().print(wm);
			logger.info(jsonMessage);

			WrapperMessage.Builder builder = WrapperMessage.newBuilder();
			JsonFormat.parser().merge(jsonMessage, builder);
			wm = builder.build();
			LoginRequest acquiredLoginRequest = wm.getLoginRequest();
			Assert.assertEquals(acquiredLoginRequest.getUserName(), loginRequest.getUserName());
			Assert.assertEquals(acquiredLoginRequest.getPassword(), loginRequest.getPassword());

		} catch (InvalidProtocolBufferException e) {
			logger.log(Level.SEVERE, "InvalidProtocolBufferException", e);
			Assert.fail();
		}
	}
	
	@Test
	public void testLoginReponseJson() {
		try {
			LoginResponse loginResponse = LoginResponse.newBuilder().setUserName("user").addRole("user").addRole("admin").build();
			LoginResponseJsonProvider loginResponseJsonProvider = new LoginResponseJsonProvider();
			ByteArrayOutputStream entityOutputStream = new ByteArrayOutputStream();
			loginResponseJsonProvider.writeTo(loginResponse, LoginRequest.class, null, null, null, null, entityOutputStream);
			byte[] rawData = entityOutputStream.toByteArray();
			String jsonString = new String(rawData, Charsets.UTF_8);
			logger.info("*:" + jsonString + ":*");	
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
			Assert.fail();
		}
	}
	
	@DataProvider(name = "testDeviceEventWrapperProvider")
	public static Object[][] testDeviceEventWrapperProvider() {
		return new Object[][] {
			{ "device1", new Integer(1), new Boolean(true) },
			{ "device1", new Integer(1), new Boolean(false) },
			{ "device1", new Integer(0), new Boolean(true) },
			{ "device1", new Integer(0), new Boolean(false) },
		};
	}
	
	@Test(dataProvider = "testDeviceEventWrapperProvider")
	public void testDeviceEventMessage(String deviceId, int pinId, boolean pinState) {
		try {
			WrapperMessage wm;
			JsonElement jsonElement;
			
			logger.info("deviceId=" + deviceId + " pinId=" + pinId + " pinState=" + pinState);
			ControlOutputEvent controlOutputEvent = ControlOutputEvent.newBuilder().setPinId(pinId).setState(pinState).build();
			DeviceEvent deviceEvent = DeviceEvent.newBuilder().setDeviceId(deviceId).setControlOutputEvent(controlOutputEvent).build();
			wm = WrapperMessage.newBuilder().setDeviceEvent(deviceEvent).build();
		
			String jsonString = JsonFormat.printer().includingDefaultValueFields().print(wm);
			logger.info(jsonString);
			jsonElement = new JsonParser().parse(jsonString);
			JsonElement deviceEventJson = jsonElement.getAsJsonObject().get("deviceEvent");
			Assert.assertNotNull(deviceEventJson);
			JsonElement deviceIdJson = deviceEventJson.getAsJsonObject().get("deviceId");
			Assert.assertNotNull(deviceIdJson);
			Assert.assertEquals(deviceIdJson.getAsString(), deviceId);
			JsonElement coeJson = deviceEventJson.getAsJsonObject().get("controlOutputEvent");
			Assert.assertNotNull(coeJson);
			JsonElement pinIdJson = coeJson.getAsJsonObject().get("pinId");
			Assert.assertNotNull(pinIdJson);
			Assert.assertTrue(pinIdJson.getAsInt() == pinId);
			JsonElement stateJson = coeJson.getAsJsonObject().get("state");
			Assert.assertNotNull(stateJson);
			Assert.assertTrue(stateJson.getAsBoolean() == pinState);
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
			Assert.fail();
		}
	}
	

}
