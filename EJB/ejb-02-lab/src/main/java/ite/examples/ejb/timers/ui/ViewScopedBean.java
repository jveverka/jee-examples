package ite.examples.ejb.timers.ui;

import ite.examples.ejb.timers.SingletonScopedBean;
import ite.examples.ejb.timers.StatelessBean;

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
	
	@Inject
	private StatelessBean stateless;

	@Inject
	private SingletonScopedBean singleton;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public int getSingletonCounter() {
		return singleton.getCounter();
	}

	public int getStatelessCounter() {
		return stateless.getCounter();
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
