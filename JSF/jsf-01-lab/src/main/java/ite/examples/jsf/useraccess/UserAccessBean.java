package ite.examples.jsf.useraccess;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("userAccess")
public class UserAccessBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserAccessBean.class.getName());
	
	private String userName;
	private String password;
	private boolean validUser;
	
	@Inject
	private UserDataService userService;

	@PostConstruct
	private void init() {
		logger.info("init ...");
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
		return validUser;
	}
	
	public void loginAction() {
		logger.info("loginAction ...");
		validUser = userService.validateUser(userName, password);
		if (!validUser) {
			logger.info("loginAction: FAILED!");
			userName = null;
			password = null;
		} else {
			logger.info("loginAction: OK!");
			password = null;
		}
	}
	
	public void logoutAction() {
		logger.info("logoutAction for user: " + userName);
		validUser = false;
		userName = null;
		password = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
