package ite.online.training.ui.content;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ite.online.training.UserVerificationService;

@SessionScoped
@Named("userAccess")
public class UserAccessBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserAccessBean.class.getName());

	private String userName;
	private String password;
	private boolean validUser;
	
	@Inject
	private UserVerificationService uvs;
	
	@PostConstruct
	private void init() {
		validUser = false;
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
	
	public String doLoginAction() {
		if (uvs.isUserAuthorized(userName, password)) {
			logger.info("user " + userName + " authorized OK !");
			validUser = true;
			password = null;
		} else {
			logger.info("user " + userName + " NOT authorized !");
			validUser = false;
			password = null;
		}
		return "contentIndex";
	}
	
	public String doLogoutAction() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		validUser = false;
		password = null;
		return "contentIndex";
	}

}
