package ite.examples.cdi.producer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("requestScoped")
public class RequestScopedBean {
	
	@Inject
	private Logger logger;
	
	@Inject 
	private StateHolder sh;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		logger.info("StateHolder: " + sh.getState());
		sh.setState("setWorkingState");
	}
	
	public String getShState() {
		return sh.getState();
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
