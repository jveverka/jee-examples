package ite.example.services.ws.dto;

public class ResponseMessage {
	
	private long timestamp;
	private long id;
	private String message;
	private int returnCode;
	private String sessionId;
	
	public ResponseMessage() {
	}

	public ResponseMessage(long timestamp, long id, String message, int returnCode, String sessionId) {
		this.timestamp = timestamp;
		this.id = id;
		this.message = message;
		this.sessionId = sessionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "RESP:" + timestamp + ":" + id + ":" + message + ":" + returnCode + ":" + sessionId;
	}

	public String toJsonString() {
		return "{ \"timestamp\": " + timestamp + ", \"id\": " + id + ", \"message\": \"" + message + "\", \"returncode\": \"" + returnCode + "\", \"sessionid\": \"" + sessionId + "\" }";
	}

}
