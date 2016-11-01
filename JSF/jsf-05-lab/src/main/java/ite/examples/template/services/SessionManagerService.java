package ite.examples.template.services;

import javax.ejb.Local;

@Local
public interface SessionManagerService {
	
	public void addSession(SessionRecord sessionRecord);
	
	public long getSessionTimeout(String sessionId);

	public boolean isValidSession(String sessionId);

	public void updateSession(String sessionId);

	public void removeSession(String sessionId);

}
