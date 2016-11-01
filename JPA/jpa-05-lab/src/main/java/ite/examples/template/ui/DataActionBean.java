package ite.examples.template.ui;

import ite.examples.template.services.DataAccessService;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("dataAction")
public class DataActionBean implements Serializable {

	private static final Logger logger = Logger.getLogger(DataActionBean.class.getName());
	
	@Inject
	private DataAccessService das;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public void startAction() {
		das.createEmployeeAction();
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
