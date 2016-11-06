package ite.example.services;

import java.util.List;

import javax.ejb.Local;
import javax.websocket.Session;

import ite.example.services.dto.SessionInfo;

@Local
public interface SessionManager {
	
	public void addSession(Session session);
	
	public void removeSession(String sessionId);
	
	public void sendMessage(String sessionId, String message);
	
	public List<SessionInfo> getActiveSessionInfo();
	
}
