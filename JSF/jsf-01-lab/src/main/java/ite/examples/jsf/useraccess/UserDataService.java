package ite.examples.jsf.useraccess;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class UserDataService {
	
	private static final Logger logger = Logger.getLogger(UserDataService.class.getName());
	private Map<String, String> credentials;

	@PostConstruct
	private void init() {
		logger.info("init ...");
		credentials = new HashMap<>();
		credentials.put("user1", "secret1");
		credentials.put("user2", "secret2");
		credentials.put("user3", "secret3");
	}
	
	public boolean validateUser(String userName, String password) {
		logger.info("validateUser: " + userName);
		String passwordFromCache = credentials.get(userName);
		if (passwordFromCache != null && passwordFromCache.equals(password)) {
			return true;
		}
		return false;
	}
	
}
