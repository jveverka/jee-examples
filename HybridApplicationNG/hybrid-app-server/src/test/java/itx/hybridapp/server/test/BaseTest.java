package itx.hybridapp.server.test;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.mockito.internal.util.reflection.Whitebox;

import itx.hybridapp.server.services.devices.DataSeriesBufferImpl;
import itx.hybridapp.server.services.devices.DeviceService;
import itx.hybridapp.server.services.devices.DeviceServiceImpl;
import itx.hybridapp.server.services.useraccess.UserAccessService;
import itx.hybridapp.server.services.useraccess.UserAccessServiceImpl;
import itx.hybridapp.server.services.useraccess.UserCredentialsServiceImpl;

public abstract class BaseTest {
	
	protected UserAccessServiceImpl createUserAccessService() {
		UserCredentialsServiceImpl ucService = new UserCredentialsServiceImpl();
		ucService.init();
		UserAccessServiceImpl uaService = new UserAccessServiceImpl();
		Whitebox.setInternalState(uaService, "ucService", ucService);
		uaService.init();
		return uaService;
	}
	
	protected DeviceService createDeviceService(UserAccessService uaService) {
		DataSeriesBufferImpl dsBuffer = new DataSeriesBufferImpl();
		dsBuffer.init();
		DeviceServiceImpl deviceService = new DeviceServiceImpl();
		deviceService.init();
		Whitebox.setInternalState(deviceService, "dsBuffer", dsBuffer);
		Whitebox.setInternalState(deviceService, "messagePublisher", uaService);
		return deviceService;
	}
	
	protected Session createWsSession(String id) {
		return new MockSession(id);
	}

	protected HttpSession createHttpSession(String id) {
		return new MockHttpSession(id);
	}

}
