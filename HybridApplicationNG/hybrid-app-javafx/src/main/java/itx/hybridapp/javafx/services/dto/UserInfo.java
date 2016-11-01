package itx.hybridapp.javafx.services.dto;

import java.util.List;

public class UserInfo {
	
	private String userName;
	private String password;
	private String httpSessionId;
	private String normalizedHttpSessionId;
	private List<String> roles;

	public UserInfo(String userName, String password, String httpSessionId, String normalizedHttpSessionId, List<String> roles) {
		super();
		this.userName = userName;
		this.password = password;
		this.httpSessionId = httpSessionId;
		this.normalizedHttpSessionId = normalizedHttpSessionId;
		this.roles = roles;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getHttpSessionId() {
		return httpSessionId;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public String getNormalizedHttpSessionId() {
		return normalizedHttpSessionId;
	}

}
