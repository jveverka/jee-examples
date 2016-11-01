package itx.hybridapp.common.client.rest;

import java.util.List;

import javax.security.auth.login.LoginException;

public interface UserAccessClient {

	public static UserAccessClient buildClient(String baseURL, String mediaType) {
		return new UserAccessClientImpl(baseURL, mediaType);
	}
	
	public List<String> login(String userName, String password) throws LoginException;
	
	public boolean isValidSession();
	
	public String getHttpSessionId();
	
	public void logout();
	
}
