package ite.examples.scopes.cdi;

import ite.examples.scopes.utils.AdminBean;
import ite.examples.scopes.utils.IdGenerator;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.logging.Logger;

@SessionScoped
@Named("sscoped")
public class SessionScopedBean implements Serializable {

	private static final Logger logger = Logger.getLogger(SessionScopedBean.class.getName());
	private int myId;
	private String name;
	
	@Inject
	private AdminBean admin;
	
	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "SessionScopedBean";
		logger.info("init [" + myId + "] ...");
		admin.registerSessionScoped();
	}
	
	public int getId() {
		return myId;
	}

	public String getName() {
		return name;
	}
	
	@PreDestroy
	private void deinit() {
		admin.unregisterSessionScoped();
		logger.info("deinit [" + myId + "] ...");
	}

}
