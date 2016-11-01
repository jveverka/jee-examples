package ite.examples.template.ui;

import ite.examples.template.services.SessionManagerService;
import ite.examples.template.services.SessionRecord;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@SessionScoped
@Named("userAccess")
public class UserAccessBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(UserAccessBean.class.getName());
	
	private String password;
	private String userName;
	private boolean validUser;
	private String sessionId;
	
	@Inject 
	private SessionManagerService sessionManager;
	
	@PostConstruct
	private void init() {
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isValidUser() {
		return validUser;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public String doLoginAction() {
		if ("user".equals(userName) && "password".equals(password)) {
			HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			sessionId = session.getId();
			SessionRecord sessionRecord = new SessionRecord(session.getId(), session.getLastAccessedTime(), session.getCreationTime(), session.getMaxInactiveInterval());
			logger.info("sessionManager is null: " + (sessionManager == null));
			SessionMonitorObject sessionMonitor = new SessionMonitorObject(sessionId, sessionManager, sessionRecord);
			session.setAttribute("sessionMonitor", sessionMonitor);
			validUser = true;
			return "index";
		} 
		validUser = false;
		return "login";
	}

	public String doLogoutAction() {
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		validUser = false;
		return "";
	}

	public String doSubmitAction() {
		logger.info("doSubmitAction ...");
		return "";
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
