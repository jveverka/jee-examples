package ite.online.training;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class UserVerificationServiceImpl implements UserVerificationService {
	
	private Map<String, String> usersToConfirm;
	private Map<String, String> users;
	private Map<String, String> admins;
	
	@PostConstruct
	private void init() {
		users = new HashMap<>();
		users.put("juraj", "xxx");
		users.put("ivan", "xxx");
		admins = new HashMap<>();
		admins.put("admin", "xx");
		usersToConfirm = new HashMap<>(); 
	}

	@Override
	public boolean isUserAuthorized(String userName, String password) {
		String passwd = users.get(userName);
		if (passwd != null && passwd.equals(password)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isAdminAuthorized(String userName, String password) {
		String passwd = admins.get(userName);
		if (passwd != null && passwd.equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean changeUserPassword(String userName, String oldPassword, String newPassword) {
		if (isUserAuthorized(userName, oldPassword)) {
			users.put(userName, newPassword);
			return true;
		}
		return false;
	}

	@Override
	public boolean changeAdminPassword(String userName, String oldPassword, String newPassword) {
		if (isAdminAuthorized(userName, oldPassword)) {
			admins.put(userName, newPassword);
			return true;
		}
		return false;
	}

	@Override
	public String createUser(String userName) {
		if (usersToConfirm.get(userName) != null) {
			return null;
		}
		String token = UUID.randomUUID().toString();
		usersToConfirm.put(userName, token);
		return token;
	}

	@Override
	public boolean confirmUser(String userName, String confirmToken) {
		String token = usersToConfirm.get(userName);
		if (token != null && token.equals(confirmToken)) {
			usersToConfirm.remove(userName);
			users.put(userName, token);
		}
		return false;
	}

	@Override
	public boolean deleteUser(String userName, String password) {
		if (isUserAuthorized(userName, password)) {
			users.remove(userName);
		}
		return false;
	}

}
