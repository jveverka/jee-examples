package itx.hybridapp.common.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import itx.hybridapp.common.providers.dsprotocol.TestRequestJsonProvider;
import itx.hybridapp.common.providers.dsprotocol.TestRequestProtobufProvider;
import itx.hybridapp.common.providers.dsprotocol.TestResponseJsonProvider;
import itx.hybridapp.common.providers.dsprotocol.TestResponseProtobufProvider;
import itx.hybridapp.common.providers.uaprotocol.KillHttpSessionRequestJsonProvider;
import itx.hybridapp.common.providers.uaprotocol.KillHttpSessionRequestProtobufProvider;
import itx.hybridapp.common.providers.uaprotocol.KillWsSessionRequestJsonProvider;
import itx.hybridapp.common.providers.uaprotocol.KillWsSessionRequestProtobufProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginRequestJsonProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginRequestProtobufProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginResponseJsonProvider;
import itx.hybridapp.common.providers.uaprotocol.LoginResponseProtobufProvider;


public final class ClientUtils {

	public static final String LOGIN_URL = "/useraccess/login"; 
	public static final String ISVALIDSESSION_URL = "/useraccess/isValidSession"; 
	public static final String LOGOUT_URL = "/useraccess/logout"; 
	public static final String TESTDATA_URL = "/dataservice/getData"; 
	public static final String WSENDPOINT_URL = "/ws/wsendpoint";
	public static final String KILLWSSESSION_URL = "/useraccess/killWsSession"; 
	public static final String KILLHTTPSESSION_URL = "/useraccess/killHttpSession"; 
	
	public static final String JSESSIONID = "JSESSIONID";

	public static Client createHttpClient() {
		return ClientBuilder.newBuilder()
			.register(LoginRequestProtobufProvider.class)
			.register(LoginResponseProtobufProvider.class)
			.register(LoginRequestJsonProvider.class)
			.register(LoginResponseJsonProvider.class)
			.register(TestRequestJsonProvider.class)
			.register(TestRequestProtobufProvider.class)
			.register(TestResponseJsonProvider.class)
			.register(TestResponseProtobufProvider.class)
			.register(KillHttpSessionRequestProtobufProvider.class)
			.register(KillWsSessionRequestProtobufProvider.class)
			.register(KillHttpSessionRequestJsonProvider.class)
			.register(KillWsSessionRequestJsonProvider.class)
			.build();
	}
	
}
