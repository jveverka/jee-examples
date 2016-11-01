package ite.examples.template.services.ws;

public class GetDataRequest {
	
	private String sessionId;

	public GetDataRequest() { 
	}

	public GetDataRequest(String sessionId) { 
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
