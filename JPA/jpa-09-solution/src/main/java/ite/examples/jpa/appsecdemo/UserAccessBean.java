package ite.examples.jpa.appsecdemo;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jv.appsec.SecurityRealmException;
import org.jv.appsec.SecurityRealmService;
import org.jv.appsec.UserData;

@SessionScoped
@Named("userAccess")
public class UserAccessBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserAccessBean.class.getName());
	
	private String userName;
	private String password;
	private UserData userData;
	
	@Inject
	private SecurityRealmService secRealm;

	@PostConstruct
	private void init() {
		logger.info("init ...");
		userData = null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isValidUser() {
		return userData != null;
	}
	
	public String getUserRoles() {
		if (isValidUser()) {
			String roles = "";
			for (String roleId: userData.getRoleIds()) {
				roles = roles + "" + roleId;
			}
			return roles;
		}
		return "";
	}
	
	public void loginAction() {
		logger.info("loginAction ...");
		try {
			userData = secRealm.getUserData(userName, password);
			if (userData == null) {
				logger.info("loginAction: FAILED!");
				userName = null;
				password = null;
			} else {
				logger.info("loginAction: OK!");
				password = null;
			}
		} catch (SecurityRealmException e) {
			logger.severe("SecurityRealmException: " + e.getMessage());
		}
	}
	
	public void logoutAction() {
		logger.info("logoutAction for user: " + userName);
		userData = null;
		userName = null;
		password = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
