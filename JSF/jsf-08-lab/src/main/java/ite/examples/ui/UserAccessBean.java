package ite.examples.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import ite.examples.services.access.UserAccessService;

@SessionScoped
@Named("userAccess")
public class UserAccessBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(UserAccessBean.class.getName());

	private String userName;
	private String password;
	
	@Inject
	private UserAccessService userService;
	
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
	
	public boolean isAuthorized() {
		return userService.isAuthorized();
	}
	
	public String doLoginAction() {
		logger.info("doLoginAction ...");
		boolean authOk = userService.doLoginAction(userName, password);
		logger.info("doLoginAction:  " + userService.isAuthorized());
		password = null;
		if (authOk) {
			return "index";
		}
		return "";
	}
	
	public String doLogoutAction() {
		logger.info("doLoginAction ...");
		userService.doLogoutAction();
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		return "login";
	}

}
