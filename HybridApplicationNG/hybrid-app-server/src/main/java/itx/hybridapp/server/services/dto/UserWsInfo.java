package itx.hybridapp.server.services.dto;

import java.util.List;

public class UserWsInfo {
	
	private String wsSessionId;
	private String userName;
	private String protocol;
	private List<String> roles;
	
	public UserWsInfo() {
	}

	public UserWsInfo(String wsSessionId, String userName, String protocol, List<String> roles) {
		super();
		this.wsSessionId = wsSessionId;
		this.userName = userName;
		this.roles = roles;
		this.protocol = protocol;
	}

	public String getWsSessionId() {
		return wsSessionId;
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
