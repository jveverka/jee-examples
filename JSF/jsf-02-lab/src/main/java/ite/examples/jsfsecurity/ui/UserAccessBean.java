package ite.examples.jsfsecurity.ui;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@SessionScoped
@Named("userAccess")
public class UserAccessBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(UserAccessBean.class.getName());
	
	private String userName;
	private String password;
	private String message;
	private boolean validUser;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		message = "please login !";
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
	
	public String getMessage() {
		return message;
	}
	
	public boolean isValidUser() {
		return validUser;
	}
	
	public void doLoginAction() {
		logger.info("doLoginAction " + userName + " ...");
		FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
       		request.login(userName, password);
       		externalContext.getSessionMap().put("userName", userName);
       		message = "login OK !";
       		validUser = true;
       		//externalContext.redirect(originalURL);
       		logger.info("login OK !");
       		return;
        } catch (ServletException e) {
            // Handle unknown username/password in request.login().
        	logger.severe("ServletException: " + e.getMessage());
        }
    	message = "login failed !";
    	validUser = false;
		logger.info("login FAILED !");
	}
	
	public void doLogoutAction() {
		logger.info("doLogoutAction " + userName + " ...");
        try {
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.invalidateSession();
            validUser = false;
            message = "logout OK";
			externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
		} catch (IOException e) {
			logger.severe("IOException: " + e.getMessage());
		}
	}
	
}
