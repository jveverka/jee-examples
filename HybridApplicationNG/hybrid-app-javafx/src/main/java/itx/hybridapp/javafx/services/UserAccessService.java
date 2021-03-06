package itx.hybridapp.javafx.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import itx.hybridapp.common.client.rest.UserAccessClientImpl;
import itx.hybridapp.javafx.services.dto.UserInfo;

public class UserAccessService extends UserAccessClientImpl {
	
	private static final Logger logger = Logger.getLogger(UserAccessService.class.getName());
	
	private String userName;
	private String password;
	private String httpSessionId;
	private String normalizedHttpSessionId;
	private List<String> roles;
	
	private UserAccessService(String baseUrl, String mediaType) {
		super(baseUrl ,mediaType);
	}
	
	public static UserAccessService getNewInstance(String baseUrl, String mediaType) {
		return new UserAccessService(baseUrl, mediaType);
	}
	
	public List<String> login(String userName, String password) throws LoginException {
		this.roles = super.login(userName, password);
		this.userName = userName;
		this.password = password;
		this.httpSessionId = this.getHttpSessionId();
		this.normalizedHttpSessionId = normalizeHttpSessionId(httpSessionId);
		logger.info("login: " + userName + " " + httpSessionId);
		return this.roles;
	}
	
	private String normalizeHttpSessionId(String httpSessionId) {
		logger.info("normalize: " + httpSessionId);
		return httpSessionId.split("\\.")[0];
	}
	
	public UserInfo getUserInfo() {
		return new UserInfo(userName, password, httpSessionId, normalizedHttpSessionId, new ArrayList<String>(roles));
	}

}
