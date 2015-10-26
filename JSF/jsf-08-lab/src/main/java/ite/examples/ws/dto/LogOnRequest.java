package ite.examples.ws.dto;

public class LogOnRequest {
	
	private String userName;
	private String password;
	

	public LogOnRequest() {
	}

	public LogOnRequest(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
