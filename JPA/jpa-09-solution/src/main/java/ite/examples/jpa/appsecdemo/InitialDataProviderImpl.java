package ite.examples.jpa.appsecdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import org.jv.appsec.InitialDataProvider;
import org.jv.appsec.RealmType;
import org.jv.appsec.RoleData;
import org.jv.appsec.UserData;

@Dependent
public class InitialDataProviderImpl implements InitialDataProvider {

	@Override
	public String getDefaultAdminRolePropertyId() {
		return "superuser";
	}

	@Override
	public UserData getAdminUserData() {
		Map<String, Boolean> properties = new HashMap<>();
		properties.put("superuser", true);
		properties.put("canread", true);
		properties.put("canwrite", true);
		RoleData rd = new RoleData("admins", properties);
		Map<String, RoleData> roles = new HashMap<>();
		roles.put(rd.getName(), rd);
		UserData ud = new UserData("admin", "Admin", "Administrator", "", "", RealmType.LOCAL, roles);
		return ud;
	}

	@Override
	public String getDefaultAdminPassword() {
		return "topsecret";
	}

	@Override
	public String getDefaultPassword(String userId) {
		return "secret";
	}

	@Override
	public List<RoleData> getInitialRoles() {
		return null;
	}

	@Override
	public List<UserData> getInitialUsers() {
		List<UserData> users = new ArrayList<>();
		Map<String, Boolean> properties = new HashMap<>();
		properties.put("canread", true);
		RoleData rd = new RoleData("guests", properties);
		Map<String, RoleData> roles = new HashMap<>();
		roles.put(rd.getName(), rd);
		UserData ud = new UserData("guest", "Guest", "", "", "", RealmType.LOCAL, roles);
		users.add(ud);
		properties = new HashMap<>();
		properties.put("canread", true);
		properties.put("canwrite", true);
		rd = new RoleData("users", properties);
		roles = new HashMap<>();
		roles.put(rd.getName(), rd);
		ud = new UserData("user", "User", "", "", "", RealmType.LOCAL, roles);
		users.add(ud);
		return users;
	}

}
