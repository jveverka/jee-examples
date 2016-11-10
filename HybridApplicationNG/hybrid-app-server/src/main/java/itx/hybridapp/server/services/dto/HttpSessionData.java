package itx.hybridapp.server.services.dto;

import java.util.List;

import javax.servlet.http.HttpSession;

public class HttpSessionData {
	
	private HttpSession httpSession;
	private String userName;
	private String protocol;
	private List<String> roles;
	
	public HttpSessionData() {
	}

	public HttpSessionData(HttpSession httpSession, String userName, String protocol, List<String> roles) {
		super();
		this.httpSession = httpSession;
		this.userName = userName;
		this.roles = roles;
		this.protocol = protocol;
	}

	public String getHttpSessionId() {
		return httpSession.getId();
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
	
	public void logout() {
		this.httpSession.invalidate();
	}

}
