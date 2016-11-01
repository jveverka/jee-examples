package ite.servlet.filterauth.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import ite.servlet.filterauth.dto.UserInfo;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class AuthServiceImpl implements AuthService {
	
	private static final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());
	
	private Map<String, String> users;
	private Map<String, List<String>> roles;
	private Map<String, String> userList;
	
	@PostConstruct
	private void init() {
		logger.info("init ..");
		List<String> userRoles;
		userList = new ConcurrentHashMap<>();
		users = new ConcurrentHashMap<>();
		roles = new ConcurrentHashMap<>();
		//define users
		users.put("demo", "demo");
		users.put("user", "user");
		users.put("admin", "admin");
		//define roles
		userRoles = new ArrayList<>();
		userRoles.add("demo");
		userRoles.add("user");
		roles.put("demo", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("demo");
		userRoles.add("user");
		roles.put("user", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("admin");
		userRoles.add("root");
		userRoles.add("user");
		roles.put("admin", userRoles);
	}

	@Override
	public List<String> login(String userName, String password, String sessionId) throws LoginException {
		logger.info("login: " + userName + " " + sessionId);
		String passwd = users.get(userName);
		if (passwd != null && passwd.equals(password)) {
			userList.put(sessionId, userName);
			logger.info("login: OK");
			return Collections.unmodifiableList(roles.get(userName));
		} else {
			logger.info("login: FAILED");
			throw new LoginException("loginfor user " + userName + " has failed");
		}
	}

	@Override
	public boolean isValidSession(String sessionId) {
		if (userList.get(sessionId) != null) {
			logger.info("isValidSession: " + sessionId + " true");
			return true;
		} else {
			logger.info("isValidSession: " + sessionId + " false");
		}
		return false;
	}

	@Override
	public void logout(String sessionId) {
		logger.info("logout: " + sessionId);
		userList.remove(sessionId);
	}

	@Override
	public void renameSession(String oldSessionId, String newSessionId) {
		logger.info("renameSession: " + oldSessionId + " -> " + newSessionId);
		String userName = userList.remove(oldSessionId);
		if (userName != null) {
			userList.put(newSessionId, userName);
		}
	}

	@Override
	public UserInfo getRoles(String sessionId) {
		logger.info("getRoles: " + sessionId);
		String userName = userList.get(sessionId);
		if (userName != null) {
			return new UserInfo(userName, roles.get(userName));
		}
		return null;
	}

}
