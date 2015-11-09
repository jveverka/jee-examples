package ite.clusterdemo.services.dto;

import java.io.Serializable;
import java.util.Date;

public class SessionInfo implements Serializable {
	
	private String sessionId;
	private Date started;
	private String serverId;
	
	public SessionInfo() {
	}

	public SessionInfo(String sessionId, Date started, String serverId) {
		this.sessionId = sessionId;
		this.started = started;
		this.serverId = serverId;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public Date getStarted() {
		return started;
	}
	
	public void setStarted(Date started) {
		this.started = started;
	}
	
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Override
	public String toString() {
		return "SI:" + sessionId + ":" + started.getTime() + ":" + serverId;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof SessionInfo) {
			SessionInfo otherSession = (SessionInfo)other;
			return sessionId.equals(otherSession.getSessionId());
		}
		return false;
	}
	
}
