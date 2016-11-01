package org.jv.appsec;

import java.util.List;

import javax.ejb.Local;

@Local
public interface InitialDataProvider {
	
	public String getDefaultAdminRolePropertyId();
	
	public UserData getAdminUserData();
	
	public String getDefaultAdminPassword();
	
	public String getDefaultPassword(String userId);
	
	public List<RoleData> getInitialRoles();
	
	public List<UserData> getInitialUsers(); 

}
