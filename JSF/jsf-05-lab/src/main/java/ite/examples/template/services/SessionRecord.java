package ite.examples.template.services;

public class SessionRecord {
	
	private String sessionId;
	private long lastAccessTime;
	private long created;
	private int maxInactiveInterval;
	
	public SessionRecord(String sessionId, long lastAccessTime, long created,
			int maxInactiveInterval) {
		super();
		this.sessionId = sessionId;
		this.lastAccessTime = lastAccessTime;
		this.created = created;
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}
	
	@Override
	public String toString() {
		return sessionId;
	}
	

}
