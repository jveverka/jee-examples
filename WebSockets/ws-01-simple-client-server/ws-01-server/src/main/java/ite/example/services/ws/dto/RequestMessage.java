package ite.example.services.ws.dto;

public class RequestMessage {

	private long timestamp;
	private long id;
	private String message;
	
	public RequestMessage() {
	}

	public RequestMessage(long timestamp, long id, String message) {
		this.timestamp = timestamp;
		this.id = id;
		this.message = message;
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

	@Override
	public String toString() {
		return "RQST:" + timestamp + ":" + id + ":"+ message;
	}
	
	public String toJsonString() {
		return "{ \"timestamp\": " + timestamp + ", \"id\": " + id + ", \"message\": \"" + message + "\" }";
	}

}
