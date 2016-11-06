package ite.example.services.dto;

public class SessionInfo {
	
	private String id;
	private long idleTimeout;
	
	public SessionInfo(String id, long idleTimeout) {
		this.id = id;
		this.idleTimeout = idleTimeout; 
	}
	
	public String getId() {
		return id;
	}

	public long getIdleTimeout() {
		return idleTimeout;
	}

}
