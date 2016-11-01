package org.jv.appsec;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SecurityRealmService {

	public UserData getUserData(String userId, String password) throws SecurityRealmException;

	public boolean changeUserPassword(String userId, String oldPassword, String newPassword) throws SecurityRealmException;
	
	public void persistUserData(UserData userData, String password, String adminId, String adminPwd) throws SecurityRealmException;
	
	public void removeUserData(String userId, String adminId, String adminPwd) throws SecurityRealmException;
	
	public List<UserData> getAllUsers(String adminId, String adminPwd) throws SecurityRealmException;

	public boolean hasUserRole(String userId, String password, String roleId);
	
	public boolean isAdminUser(String adminId, String adminPwd);
	
	public List<RoleData> getRolesData(String adminId, String adminPwd) throws SecurityRealmException;
	
	public boolean validateUserName(String userName, String adminId, String adminPwd) throws SecurityRealmException;
	
	public boolean validateRoleName(String roleName, String adminId, String adminPwd) throws SecurityRealmException;
	
	public boolean validateRolePropertyName(String roleName, String rolePropertyName, String adminId, String adminPwd) throws SecurityRealmException;

	public void persistRoleData(RoleData roleData, String adminId, String adminPwd) throws SecurityRealmException;

	public void removeRoleData(String roleId, String adminId, String adminPwd) throws SecurityRealmException;
	
}
