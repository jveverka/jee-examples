package ite.examples.scopes.jsf;

import ite.examples.scopes.cdi.DependentBean;
import ite.examples.scopes.utils.AdminBean;
import ite.examples.scopes.utils.IdGenerator;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.logging.Logger;

@ViewScoped
@Named("vscoped")
public class ViewScopedBean implements Serializable {

	private static final Logger logger = Logger.getLogger(ViewScopedBean.class.getName());
	private int myId;
	private String name;
	
	@Inject
	private AdminBean admin;
	
	@Inject
	private DependentBean dependent;

	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "ViewScopedBean";
		logger.info("init [" + myId + "] ...");
		admin.registerViewScoped();
		logger.info("dependent: " + dependent.getId());
	}
	
	public int getId() {
		return myId;
	}
	
	public String getName() {
		return name;
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit [" + myId + "] ...");
		admin.unregisterViewScoped();
	}
	
}
