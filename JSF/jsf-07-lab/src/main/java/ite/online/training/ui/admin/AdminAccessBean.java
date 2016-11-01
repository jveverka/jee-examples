package ite.online.training.ui.admin;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;
import ite.online.training.UserVerificationService;

@SessionScoped
@Named("adminAccess")
public class AdminAccessBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(AdminAccessBean.class.getName());

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
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (uvs.isAdminAuthorized(userName, password)) {
			logger.info("admin " + userName + " authorized OK !");
			validUser = true;
			password = null;
		} else {
			logger.info("admin " + userName + " NOT authorized !");
			validUser = false;
			password = null;
		}
		return "adminIndex";
	}
	
	public String doLogoutAction() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		validUser = false;
		password = null;
		return "adminIndex";
	}

}
