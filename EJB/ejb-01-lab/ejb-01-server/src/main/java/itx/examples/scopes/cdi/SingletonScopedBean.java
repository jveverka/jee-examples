package itx.examples.scopes.cdi;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;

import itx.examples.scopes.utils.AdminBean;
import itx.examples.scopes.utils.IdGenerator;

@Singleton
@Named("singleton")
public class SingletonScopedBean {
	
	private static final Logger logger = Logger.getLogger(SingletonScopedBean.class.getName());
	private int myId;
	private String name;
	
	@Inject
	private AdminBean admin;
	
	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "SingletonScopedBean";
		logger.info("init [" + myId + "] ...");
		admin.registerSingleton();
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
		admin.unregisterSingleton();
	}
	
}
