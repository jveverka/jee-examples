package ite.clusterdemo.services.topic;

import java.io.Serializable;

public class SystemEvent implements Serializable {
	
	private SystemEventType type;
	private String sessionId;
	
	public SystemEvent() {
	}

	public SystemEvent(SystemEventType type, String sessionId) {
		this.type = type;
		this.sessionId = sessionId;
	}

	public SystemEventType getType() {
		return type;
	}
	
	public void setType(SystemEventType type) {
		this.type = type;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override
	public String toString() {
		return "SYSEV:" + type.name() + ":" + sessionId;
	}

}
