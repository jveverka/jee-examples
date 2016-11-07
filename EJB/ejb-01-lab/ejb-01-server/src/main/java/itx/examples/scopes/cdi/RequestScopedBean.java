package itx.examples.scopes.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import itx.examples.scopes.utils.AdminBean;
import itx.examples.scopes.utils.IdGenerator;

import java.util.logging.Logger;

@RequestScoped
@Named("rscoped")
public class RequestScopedBean {
	
	private static final Logger logger = Logger.getLogger(RequestScopedBean.class.getName());
	private int myId;
	private String name;
	
	@Inject
	private AdminBean admin;

	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "RequestScopedBean";
		logger.info("init [" + myId + "] ...");
		admin.registerRequestScoped();
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
		admin.unregisterRequestScoped();
		//stateful.stopSession();
	}

}
