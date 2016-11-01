package ite.servlet.filterauth.dto;

import java.util.List;

public class UserInfo {
	
	private String userName;
	private List<String> roles;
	
	public UserInfo() {
	}

	public UserInfo(String userName, List<String> roles) {
		super();
		this.userName = userName;
		this.roles = roles;
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

}
