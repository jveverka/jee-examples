package itx.examples.scopes.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;

import itx.examples.scopes.utils.IdGenerator;

import java.io.Serializable;
import java.util.logging.Logger;

@Dependent
public class DependentBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(DependentBean.class.getName());
	private int myId;
	private String name;
	
	public DependentBean() {
		logger.info("INIT");
	}

	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "DependentBean";
		logger.info("init [" + myId + "] ...");
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
	}

}
