package itx.hybridapp.server.services.useraccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.security.auth.login.LoginException;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class UserCredentialsServiceImpl implements UserCredentialsService {

	private Map<String, String> users;
	private Map<String, List<String>> roles;

	@PostConstruct
	public void init() {
		List<String> userRoles;
		users = new HashMap<>();
		users.put("demo", "demo123");
		users.put("user", "user123");
		users.put("pi", "pi123");
		users.put("pisim", "pisim123");
		users.put("jfx", "jfx123");
		users.put("tablet", "tablet123");
		users.put("mobil", "mobil123");
		users.put("odroid", "odroid123");
		users.put("admin", "admin123");
		users.put("root", "root123");
		roles = new HashMap<>();
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("demo", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("user", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("pi");
		roles.put("pi", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("pisim", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("jfx", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("tablet", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("mobil", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		roles.put("odroid", userRoles);

		userRoles = new ArrayList<>();
		userRoles.add("user");
		userRoles.add("admin");
		roles.put("admin", userRoles);
		userRoles = new ArrayList<>();
		userRoles.add("user");
		userRoles.add("admin");
		roles.put("root", userRoles);
	}

	public List<String> verifyCredentials(String userName, String password) throws LoginException {
		String passwd = users.get(userName);
		if (passwd != null && passwd.equals(password)) {
			return new ArrayList<>(roles.get(userName));
		}
		throw new LoginException();
	}

}
