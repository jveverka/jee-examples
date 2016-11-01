package ite.examples.cdi.producer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class SingletonScopedBean {
	
	private Logger logger = Logger.getLogger(SingletonScopedBean.class.getName());
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
