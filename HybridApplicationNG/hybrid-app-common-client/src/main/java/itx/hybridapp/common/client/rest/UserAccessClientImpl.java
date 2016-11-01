package itx.hybridapp.common.client.rest;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import itx.hybridapp.common.client.ClientUtils;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginRequest;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginResponse;


public class UserAccessClientImpl implements UserAccessClient {

	private String baseURL;
	private String mediaType;
	private Client client;
	private NewCookie jsessionId;
	
	protected UserAccessClientImpl(String baseURL, String mediaType) {
		this.baseURL = baseURL;
		this.mediaType = mediaType;
		client = ClientUtils.createHttpClient();
	}

	@Override
	public List<String> login(String userName, String password) throws LoginException {
		WebTarget target = client.target(baseURL + ClientUtils.LOGIN_URL);
		LoginRequest loginRequest = LoginRequest.newBuilder().setUserName(userName).setPassword(password).build();
		Builder builder = target.request();
		Response response = builder.post(Entity.entity(loginRequest, mediaType));
		LoginResponse loginResponse = response.readEntity(LoginResponse.class);
		if (response.getStatus() != Status.OK.getStatusCode()) {
			throw new LoginException("client login failed");
		}
		jsessionId = response.getCookies().get(ClientUtils.JSESSIONID);
		List<String> roles = new ArrayList<>(); 
		loginResponse.getRoleList().forEach(r -> {roles.add(r);});
		return roles;
	}

	@Override
	public boolean isValidSession() {
		WebTarget target = client.target(baseURL + ClientUtils.ISVALIDSESSION_URL);
		Builder builder = target.request();
		if (jsessionId != null) {
			builder.cookie(jsessionId);
		}
		Response response = builder.post(null);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		}
		return false;
	}

	@Override
	public String getHttpSessionId() {
		return jsessionId.getValue();
	}

	@Override
	public void logout() {
		WebTarget target = client.target(baseURL + ClientUtils.LOGOUT_URL);
		Builder builder = target.request();
		if (jsessionId != null) {
			builder.cookie(jsessionId);
		}
		builder.post(null);
		jsessionId = null;
	}
	
}
