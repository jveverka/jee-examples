package ite.servlet.filterauth.service;

public class LoginException extends Exception {
	
	public LoginException() {
		super();
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Exception e) {
		super(e);
	}

}
