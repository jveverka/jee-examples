package ite.example.ui;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.example.services.SessionManager;
import ite.example.services.dto.SessionInfo;

@SessionScoped
@Named("sessions")
public class SessionViewBean implements Serializable {
	
	private String sessionId;
	private String message;
	private List<SessionInfo> activeSessions;
	
	@Inject
	private SessionManager sessionManager;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void reloadData() {
		activeSessions = sessionManager.getActiveSessionInfo();
	}
	
	public List<SessionInfo> getActiveSessions() {
		return activeSessions;
	}
	
	public void sendMessageAction() {
		sessionManager.sendMessage(sessionId, message);
	}

}
