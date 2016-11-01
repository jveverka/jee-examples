package ite.servlet.filterauth.dto;

import java.util.List;

public class LoginResponse {
	
	private List<String> roles;
	
	public LoginResponse() {
	}

	public LoginResponse(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
