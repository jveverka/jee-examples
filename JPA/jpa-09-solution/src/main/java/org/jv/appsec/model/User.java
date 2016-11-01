package org.jv.appsec.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.jv.appsec.RealmType;
import org.jv.appsec.RoleData;
import org.jv.appsec.UserData;

@Entity
@Table(name="APPSEC_USERS")
public class User {
	
	@Id
	private String name;
	private String firstName;
	private String secondName;
	private String email;
	private String phoneNumber;
	private RealmType realmType;
	
	@ManyToMany(targetEntity=Role.class)
	@JoinTable(name="APPSEC_USERS_ROLES")
	private Map<String, Role> roles; 

	public User(String name, String firstName, String secondName, String email,
			String phoneNumber, RealmType realmType,  Map<String, Role> roles) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.realmType = realmType;
		this.roles = roles;
	}

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public RealmType getRealmType() {
		return realmType;
	}

	public void setRealmType(RealmType realmType) {
		this.realmType = realmType;
	}

	public Map<String, Role> getRoles() {
		return roles;
	}
	
	public UserData getUserData() {
		Map<String, RoleData> roleData = new HashMap<String, RoleData>();
		for (String roleName: roles.keySet()) {
			roleData.put(roleName, roles.get(roleName).getRoleData());
		}
		return new UserData(name, firstName, secondName, email, phoneNumber, realmType, roleData);
	}
}
