package itx.examples.scopes.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import itx.examples.scopes.utils.AdminBean;
import itx.examples.scopes.utils.IdGenerator;

import java.util.logging.Logger;


@ApplicationScoped
@Named("ascoped")
public class ApplicationScopedBean {
	
	private static final Logger logger = Logger.getLogger(ApplicationScopedBean.class.getName());
	private int myId;
	private String name;
	
	@Inject
	private AdminBean admin;
	
	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "ApplicationScopedBean";
		logger.info("init [" + myId + "] ...");
		admin.registerAppScoped();
	}

	public int getId() {
		return myId;
	}
	
	public String getName() {
		return name;
	}
	
	@PreDestroy
	private void deinit() {
		admin.unregisterAppScoped();
		logger.info("deinit [" + myId + "] ...");
	}


}
