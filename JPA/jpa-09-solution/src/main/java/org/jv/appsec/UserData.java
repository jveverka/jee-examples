package org.jv.appsec;

import java.util.Map;
import java.util.Set;

public class UserData {

	private String name;
	private String firstName;
	private String secondName;
	private String email;
	private String phoneNumber;
	private RealmType realmType;
	private Map<String, RoleData> roles;
	
	public UserData(String name, String firstName, String secondName,
			String email, String phoneNumber, RealmType realmType,
			Map<String, RoleData> roles) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.realmType = realmType;
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public RealmType getRealmType() {
		return realmType;
	}

	public Map<String, RoleData> getRoles() {
		return roles;
	}
	
	public boolean hasRole(String roleName) {
		if (roles.get(roleName) != null) {
			return true;
		}
		return false;
	}

	public boolean hasRolePropertyActive(String roleName, String propertyName) {
		RoleData role = roles.get(roleName);
		if (role != null) {
			return role.isPropertyActive(propertyName);
		}
		return false;
	}

	public boolean hasRolePropertyActive(String propertyName) {
		for (String name: roles.keySet()) {
			if (hasRolePropertyActive(name, propertyName)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<String> getRoleIds() {
		return roles.keySet();
	}

}
