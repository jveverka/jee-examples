package itx.hybridapp.server.services.dto;

import java.util.List;

public class UserInfo {
	
	private String httpSessionId;
	private String userName;
	private String protocol;
	private List<String> roles;
	
	public UserInfo() {
	}

	public UserInfo(String httpSessionId, String userName, String protocol, List<String> roles) {
		super();
		this.httpSessionId = httpSessionId;
		this.userName = userName;
		this.roles = roles;
		this.protocol = protocol;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
