package ite.examples.template.services.ws;

public class WSResponse {
	
	private String returnCode;
	private String message;
	private long sessionTimeout;
	
	public WSResponse() {
	}

	public WSResponse(String returnCode, String message, long sessionTimeout) {
		this.returnCode = returnCode;
		this.message = message;
		this.sessionTimeout = sessionTimeout;
	}

	public String getReturnCode() {
		return returnCode;
	}
	
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public long getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

}