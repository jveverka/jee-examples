package ite.examples.services.access;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@Stateful
@SessionScoped
public class UserAccessService implements Serializable {
	
	private static final Logger logger = Logger.getLogger(UserAccessService.class.getName());
	
	private String userName;
	private boolean authorized;
	
	@PostConstruct
	public void init() {
		logger.info("init ...");
		authorized = false;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public boolean isAuthorized() {
		return authorized;
	}
	
	public boolean doLoginAction(String userName, String password) {
		//more complex login logic if required
		logger.info("doLoginAction: " + userName);
		this.userName = userName;
		authorized = true;
		return authorized;
	}

	public void doLogoutAction() {
		logger.info("doLogoutAction: " + userName);
		//more complex login logic if required
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
