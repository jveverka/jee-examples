package ite.clusterdemo.services.wsadmin;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@SessionScoped
@Stateful
public class AdminAccessBean implements Serializable {
	
	private final static Logger logger = Logger.getLogger(AdminAccessBean.class.getName());
	
	private boolean isAuthorized;
	private String userId;
	private Date loginTime;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		resetInternalState();
	}
	
	public boolean doLoginAction(String userId, String password) {
		if (isAuthorized) {
			return true;
		}
		if ("secret".equals(password)) {
			logger.info("doLoginAction OK for " + userId);
			this.isAuthorized = true;
			this.userId = userId;
			this.loginTime = new Date();
			return true;
		} else {
			logger.info("doLoginAction Failed for " + userId);
			resetInternalState();
			return false;
		}
	}
	
	public String getUserId() {
		return userId;
	}

	public Date getLoginTime() {
		return loginTime;
	}
	
	public boolean isAuthorized() {
		return isAuthorized;
	}

	public boolean doLogoutAction() {
		if (!isAuthorized) {
			return false;
		} else {
			logger.info("doLogoutAction for " + userId);
			resetInternalState();
			return true;
		}
	}
	
	private void resetInternalState() {
		this.userId = "";
		this.loginTime = null;
		this.isAuthorized = false;
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
