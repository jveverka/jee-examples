package itx.hybridapp.server.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.Session;

import org.testng.Assert;
import org.testng.annotations.Test;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.server.services.devices.DeviceService;
import itx.hybridapp.server.services.useraccess.UserAccessService;

public class DeviceServiceTest extends BaseTest {
	
	private static final Logger logger = Logger.getLogger(DeviceServiceTest.class.getName());
	
	@Test
	public void testDeviceService() {
		try {
			logger.info("testDeviceService");
			String httpSessionId = "session1";
			String wsSessionId = "wssession1";
			String deviceWsSessionId = "devicews1";
			String deviceId = "device1";
			UserAccessService uaService = createUserAccessService();
			DeviceService ds = createDeviceService(uaService);
			// web client login
			uaService.loginHttpSession(httpSessionId, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			Session wsSession = createSession(wsSessionId);
			uaService.loginWsSession(wsSession, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			//device login
			Session deviceWsSession = createSession(deviceWsSessionId);
			uaService.loginWsSession(deviceWsSession, ProtoMediaType.APPLICATION_JSON, "user", "user123");
			ds.registerDevice(deviceWsSessionId, deviceId);
			ds.publishGetDeviceStatus(deviceId, wsSessionId);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"", e);
			Assert.fail();
		}
	}

}
