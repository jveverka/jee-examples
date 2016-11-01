package ite.servlet.filterauth.service;

import java.util.List;

import javax.ejb.Local;

import ite.servlet.filterauth.dto.UserInfo;

@Local
public interface AuthService {

	public List<String> login(String userName, String password, String sessionId) throws LoginException;
	
	public boolean isValidSession(String sessionId);

	public void renameSession(String oldSessionId, String newSessionId);

	public void logout(String sessionId);
	
	public UserInfo getRoles(String sessionId);

}
