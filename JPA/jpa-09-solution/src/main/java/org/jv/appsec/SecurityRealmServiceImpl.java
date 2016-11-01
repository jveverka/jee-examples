package org.jv.appsec;

import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;

import org.jv.appsec.model.LocalCredentials;
import org.jv.appsec.model.Role;
import org.jv.appsec.model.User;
import org.jv.appsec.model.local.LocalIdentityVerificator;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SecurityRealmServiceImpl implements SecurityRealmService {
	
	private static final Logger logger = Logger.getLogger(SecurityRealmServiceImpl.class.getName());
	private String DEFAULT_ADMIN_ROLE_PROPERTY;
	
	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
    private EntityManager em;
	
	@Inject 
	private InitialDataProvider initialData;

	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void init() {
		logger.info("SecurityRealmService init...");
		DEFAULT_ADMIN_ROLE_PROPERTY = initialData.getDefaultAdminRolePropertyId();
        List<String> users = getAdminUserIdList();
		if (users == null || (users.size() == 0)) {
			logger.info("no user with default admin role property " + DEFAULT_ADMIN_ROLE_PROPERTY + " found, creating default one ...");
			UserData adminData = initialData.getAdminUserData();
			try {
				persistUserData(adminData, initialData.getDefaultAdminPassword());
			} catch (SecurityRealmException e) {
				logger.log(Level.SEVERE, "failed to create default admin ", e);
			}
			if (initialData.getInitialUsers() != null) {
				logger.info("creating initial users ...");
				for (UserData ud: initialData.getInitialUsers()) {
					try {
						persistUserData(ud, initialData.getDefaultPassword(ud.getName()));
					} catch (SecurityRealmException e) {
						logger.log(Level.SEVERE, "failed to create default user " + ud.getName(), e);
					}
				}
			}
			if (initialData.getInitialRoles() != null) {
				logger.info("creating initial roles ...");
				for (RoleData rd: initialData.getInitialRoles()) {
					try {
						persistRoleData(rd);
					} catch (SecurityRealmException e) {
						logger.log(Level.SEVERE, "failed to create default role " + rd.getName(), e);
					}
				}
			}
		} else {
			logger.info("got " + users.size() + " users with default admin role property " + DEFAULT_ADMIN_ROLE_PROPERTY);
		}
		logger.info("SecurityRealmService init done.");
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	private List<String> getAdminUserIdList() {
		logger.info("looking for users with default admin role property " + DEFAULT_ADMIN_ROLE_PROPERTY);
		String query = "SELECT DISTINCT u.name FROM appsec_users u JOIN appsec_users_roles ur ON u.name = ur.appsec_users_name JOIN appsec_roles r ON r.name = ur.roles_name JOIN appsec_role_properties rp ON r.name = rp.role_name WHERE rp.name = '" + DEFAULT_ADMIN_ROLE_PROPERTY + "' AND rp.active = true";
		logger.info("query: " + query);
		List<String> result = new ArrayList<>();
		for(Object userId : em.createNativeQuery(query).getResultList()) {
		    result.add((String) userId);
		}
		logger.info("got " + result.size() + " admin user ids");
		return result;
	}
	
	/**
	 * returns null in case user can not be authenticated by security realm
	 * in case authentication is successful, user data is returned
	 * @param userId
	 *   unique user identification
	 * @param password
	 *   plain text password string
	 * @return
	 *   instance of user data or null
	 */
	@Override
	@Transactional(rollbackOn=Exception.class)
	public UserData getUserData(String userId, String password) throws SecurityRealmException {
		logger.info("getUserData for " + userId );
		if (userId == null || userId.length() == 0) {
			logger.info("empty or null userId is not allowed");
			throw new SecurityRealmException("empty or null userId is not allowed");
		}
		if (password == null || password.length() == 0) {
			logger.info("empty or null password is not allowed");
			throw new SecurityRealmException("empty or null password is not allowed");
		}
		User user = em.find(User.class, userId);
		if (user != null) {
			if (RealmType.LOCAL.equals(user.getRealmType())) {
				logger.info("authenticating LOCAL user");
				LocalCredentials localCredentials = em.find(LocalCredentials.class, userId);
				try {
					if (LocalIdentityVerificator.verify(localCredentials, password)) {
						return user.getUserData();
					} else {
						logger.severe("authentication has failed");
						throw new SecurityRealmException("authentication has failed");
					}
				} catch (NoSuchAlgorithmException e) {
					logger.log(Level.SEVERE, "authentication has failed ! " + e.getMessage(), e);
					throw new SecurityRealmException("authentication has failed: NoSuchAlgorithmException " + e.getMessage());
				}
			} else {
				logger.severe("unsupported realm type");
				throw new SecurityRealmException("unsupported realm type");
			}
		} else {
			logger.info("user " + userId + " not found in security realm");
		}
		throw new SecurityRealmException("authentication has failed");
	}

	/**
	 * change user's password 
	 * @param userId
	 *   unique user identification
	 * @param oldPassword
	 *   user's original password
	 * @param newPassword
	 *   user's new password
	 * @return
	 */
	@Override
	@Transactional(rollbackOn=Exception.class)
	public boolean changeUserPassword(String userId, String oldPassword, String newPassword) throws SecurityRealmException {
		logger.info("changeUserPassword for " + userId );
		if (oldPassword == null || newPassword == null) {
			throw new SecurityRealmException("null passwords are not allowed");
		}
		if (oldPassword.length() == 0 || newPassword.length() == 0) {
			throw new SecurityRealmException("empty passwords are not allowed");
		}
		User user = em.find(User.class, userId);
		if (user != null) {
			if (RealmType.LOCAL.equals(user.getRealmType())) {
				LocalCredentials localCredentials = em.find(LocalCredentials.class, userId);
				try {
					if (LocalIdentityVerificator.verify(localCredentials, oldPassword)) {
						localCredentials = LocalIdentityVerificator.updateCredentials(newPassword, localCredentials);
						return true;
					} else {
						logger.severe("authentication has failed");
						throw new SecurityRealmException("authentication has failed");
					}
				} catch (NoSuchAlgorithmException e) {
					logger.log(Level.SEVERE, "authentication has failed ! " + e.getMessage(), e);
					throw new SecurityRealmException("authentication has failed: NoSuchAlgorithmException " + e.getMessage());
				}
			} else {
				logger.severe("changeUserPassword is supported only for local users");
				throw new SecurityRealmException("changeUserPassword is supported only for local users");
			} 
		} else {
			logger.severe("user " + userId + " not found in security realm");
		}
		return false;
	}

	/**
	 * create or merge user data into security realm
	 * @param userData
	 * @param password
	 * @param adminId
	 * @param adminPwd
	 * @throws SecurityRealmException
	 */
	@Override
	public void persistUserData(UserData userData, String password, String adminId, String adminPwd) throws SecurityRealmException {
		logger.info("persistUserData for " + userData.getName());
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		persistUserData(userData, password);
	}
	
	@Transactional(rollbackOn=Exception.class)
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	private void persistUserData(UserData userData, String password) throws SecurityRealmException {
		User user = em.find(User.class, userData.getName());
		Map<String, Role> roles = new HashMap<String, Role>(userData.getRoles().size());
		for (String roleName: userData.getRoles().keySet()) {
			Role role = em.find(Role.class, roleName);
			if (role == null) {
				persistRoleData(userData.getRoles().get(roleName));
				role = em.find(Role.class, roleName);
			}
			roles.put(roleName, role);
		}
		if (user == null) {
			logger.info("creating new user: " + userData.getName());
			if (!normalizeId(userData.getName()).equals(userData.getName())) {
				throw new SecurityRealmException("invalid format of role name: " + userData.getName());
			}
			user = new User(userData.getName(), userData.getFirstName(), userData.getSecondName(), userData.getEmail(),
					userData.getPhoneNumber(), userData.getRealmType(), roles);
			em.persist(user);
			if (RealmType.LOCAL.equals(userData.getRealmType())) {
				LocalCredentials localCredentials;
				try {
					localCredentials = LocalIdentityVerificator.generateCredentials(userData.getName(), password);
					em.persist(localCredentials);
				} catch (NoSuchAlgorithmException e) {
					throw new SecurityRealmException("failed to create credentials for local user " + userData.getName());
				}
			}
		} else {
			logger.info("updating existing user: " + userData.getName());
			user.setFirstName(userData.getFirstName());	
			user.setSecondName(userData.getSecondName());
			user.setEmail(userData.getEmail());
			user.setPhoneNumber(userData.getPhoneNumber());
			if (!RealmType.LOCAL.equals(userData.getRealmType())) {
				//remove user's localCredentials
				LocalCredentials localCredentials = em.find(LocalCredentials.class, userData.getName());
				if (localCredentials != null) {
					em.remove(localCredentials);
				}
			} else if (RealmType.LOCAL.equals(userData.getRealmType()) && (password != null && password.length() > 0)) {
				//update user's password
				LocalCredentials localCredentials = em.find(LocalCredentials.class, userData.getName());
				if (localCredentials != null) {
					try {
						localCredentials = LocalIdentityVerificator.updateCredentials(password, localCredentials);
						em.merge(localCredentials);
					} catch (NoSuchAlgorithmException e) {
						throw new SecurityRealmException("failed to create credentials for local user " + userData.getName());
					}
				} else {
					try {
						localCredentials = LocalIdentityVerificator.generateCredentials(userData.getName(), password);
						em.persist(localCredentials);
					} catch (NoSuchAlgorithmException e) {
						throw new SecurityRealmException("failed to create credentials for local user " + userData.getName());
					}
				}
			}
			em.merge(user);
		}
	}
	
	/**
	 * remove user data from security realm (delete user) by userId. 
	 * @param userId
	 * @param adminId
	 * @param adminPwd
	 * @throws SecurityRealmException
	 */
	@Override
	@Transactional(rollbackOn=Exception.class)
	public void removeUserData(String userId, String adminId, String adminPwd) throws SecurityRealmException {
		logger.info("removeUserData for " + userId);
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		if(isAdminUser(userId, adminPwd) && getAdminUserIdList().size() == 1) {
			throw new SecurityRealmException("can not delete last user with " + DEFAULT_ADMIN_ROLE_PROPERTY + " property (last admin user)");
		}
		User user = em.find(User.class, userId);
		if (user != null) {
			LocalCredentials localCredentials = em.find(LocalCredentials.class, userId);
			if (localCredentials != null) {
				em.remove(localCredentials);
			}
			em.remove(user);
			logger.info("user " + userId + " was removed from security realm");
		} else {
			throw new SecurityRealmException("user " + userId + " not found in security realm");
		}
	}

	/**
	 * export all user data. user data is revealed only for users with ADMIN role
	 * @param adminId
	 * @param adminPwd
	 * @return
	 * @throws SecurityRealmException
	 */
	@Override
	public List<UserData> getAllUsers(String adminId, String adminPwd) throws SecurityRealmException {
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		logger.info("getAllUsers ... ");
		List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
		List<UserData> userData = new ArrayList<UserData>(users.size());
		for (User u: users) {
			userData.add(u.getUserData());
		}
		return userData;
	}

	/**
	 * returns true if user has declared role, false otherwise
	 * @param userId
	 * @param password
	 * @param roleId
	 * @return
	 */
	@Override
	public boolean hasUserRole(String userId, String password, String roleId) {
		try {
			UserData ud = getUserData(userId, password);
			if (ud == null) {
				return false;
			}
			if (!ud.hasRole(roleId)) {
				return false;
			}
			return true;
		} catch (SecurityRealmException e) {
		}
		return false;
	}

	/**
	 * returns true if user has User Management role property, false otherwise 
	 * @param adminId
	 * @param adminPwd
	 * @return
	 */
	@Override
	public boolean isAdminUser(String adminId, String adminPwd) {
	    UserData userData;
        try {
            userData = getUserData(adminId, adminPwd);
            return userData.hasRolePropertyActive(DEFAULT_ADMIN_ROLE_PROPERTY);
        } catch (SecurityRealmException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
	    return false;
	}
	
	/**
	 * get all available role data
	 * @param adminId
	 * @param adminPwd
	 * @return
	 * @throws SecurityRealmException
	 */
	@Override
	public List<RoleData> getRolesData(String adminId, String adminPwd) throws SecurityRealmException {
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		logger.info("getRolesData ... ");
		List<Role> roles = em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
		List<RoleData> roleData = new ArrayList<RoleData>(roles.size()); 
		for (Role r: roles) {
			roleData.add(r.getRoleData());
		}
		return roleData;
	}
	
	/**
	 * validate if user name is already used as unique id for existing instance of user
	 * @param userName
	 *   user name to be validated
	 * @param adminId
	 * @param adminPwd
	 * @return
	 *   true if user name is not used as unique id in the database yet, false otherwise
	 * @throws SecurityRealmException
	 */
	@Override
	public boolean validateUserName(String userName, String adminId, String adminPwd) throws SecurityRealmException {
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		if (!normalizeId(userName).equals(userName)) {
			throw new SecurityRealmException("invalid format of user name: " + userName);
		}
		User user = em.find(User.class, userName);
		if (user == null) {
			return true;
		}
		return false;
	}
	
	/**
	 * validate if role name is already used as unique id for existing instance of role
	 * @param roleName
	 *   role name to be validated
	 * @param adminId
	 * @param adminPwd
	 * @return
	 *   true if role name is not used as unique id in the database yet, false otherwise
	 * @throws SecurityRealmException
	 */
	@Override
	public boolean validateRoleName(String roleName, String adminId, String adminPwd) throws SecurityRealmException {
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		if (!normalizeId(roleName).equals(roleName)) {
			throw new SecurityRealmException("invalid format of role name: " + roleName);
		}
		Role role = em.find(Role.class, roleName);
		if (role == null) {
			return true;
		}
		return false;
	}

	/**
	 * validate if role property name is already used as unique id for existing instance of role property within specific role
	 * @param roleName
	 *   unique role id owning role property name
	 * @param rolePropertyName
	 *   role property name to be validated
	 * @param adminId
	 * @param adminPwd
	 * @return
	 *   true if role property name is not used as unique id in the database yet, false otherwise
	 * @throws SecurityRealmException
	 */
	@Override
	public boolean validateRolePropertyName(String roleName, String rolePropertyName, String adminId, String adminPwd) throws SecurityRealmException {
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		if (!normalizeId(rolePropertyName).equals(rolePropertyName)) {
			throw new SecurityRealmException("invalid format of role property name: " + roleName + " " + rolePropertyName);
		}
		Role role = em.find(Role.class, roleName);
		if (role != null) {
			if (role.getProperties().get(rolePropertyName) != null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * persist role data 
	 * @param roleData
	 *   instance of role data to persist
	 * @param adminId
	 * @param adminPwd
	 * @throws SecurityRealmException
	 */
	@Override
	public void persistRoleData(RoleData roleData, String adminId, String adminPwd) throws SecurityRealmException {
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		persistRoleData(roleData);
	}

	@Transactional(rollbackOn=Exception.class)
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
    private void persistRoleData(RoleData roleData) throws SecurityRealmException {
		Role role = em.find(Role.class, roleData.getName());
		if (role == null) {
			logger.info("perzisting new role data: " + roleData.getName());
			if (!normalizeId(roleData.getName()).equals(roleData.getName())) {
				throw new SecurityRealmException("invalid format of role name: " + roleData.getName());
			}
			logger.info("persisting new role " + roleData.getName());
			for (String rolePropertyKey: roleData.getProperties().keySet()) {
				if (!normalizeId(rolePropertyKey).equals(rolePropertyKey)) {
					throw new SecurityRealmException("invalid format of role property name: " + roleData.getName() + " " + rolePropertyKey);
				}
			}
			Map<String, Boolean> properties = new HashMap<String, Boolean>(roleData.getProperties());
			role = new Role(roleData.getName(), properties);			
			em.persist(role);
		} else {
			logger.info("updating existing role: " + roleData.getName());
			Map<String, Boolean> properties = new HashMap<String, Boolean>(roleData.getProperties());
			role.setProperties(properties);
			em.merge(role);
		}
	}

	/**
	 * remove role data from persistence
	 * @param roleId
	 *   unique id of role data to remove
	 * @param adminId
	 * @param adminPwd
	 * @throws SecurityRealmException
	 */
	@Override
	@Transactional(rollbackOn=Exception.class)
	public void removeRoleData(String roleId, String adminId, String adminPwd) throws SecurityRealmException {
		//TODO check if removed role is used by any user in security realm
		if (!isAdminUser(adminId, adminPwd)) {
			throw new SecurityRealmException("user " + adminId + " is not authorized");
		}
		logger.info("removeRoleData ... ");
		Role role = em.find(Role.class, roleId);
		if (role != null) {
			em.remove(role);
			logger.info("role " + roleId + " has been removed from security realm");
		} else {
			throw new SecurityRealmException("role " + roleId + " not found in security realm");
		}
	}
	
	/**
	 * normalizes string id by replacing diacritics, white spaces and puctuation characters with characters from 
	 * a-z, A-Z, 0-9, _
	 * @param originalId
	 *   original id string to be normalized
	 * @return
	 *   normalized string
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public static String normalizeId(String originalId) {
		if (originalId == null) {
			return "";
		}
		String normalizedId = Normalizer.normalize(originalId, Form.NFD);
		normalizedId = normalizedId.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		normalizedId = normalizedId.replaceAll("\t|\\p{Punct}", "_");
		return normalizedId;
	}
	
}
