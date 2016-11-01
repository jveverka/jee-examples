package ite.examples.template.services;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.ejb.BeforeCompletion;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import ite.examples.data.entities.User;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TransAwareService implements Serializable {
	
	private static final Logger logger = Logger.getLogger(TransAwareService.class.getName());

	@Inject
	private DataAccessService das;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

	public User getUserById(Long id) {
		logger.info("getUserById: " + id);
		User user = das.getUserById(id);
		//user.setNick("from:TransAwareService");
		logger.info("getUserById: " + id + " done.");
		return user;
	}
	
	@AfterBegin
	public void afterBegin() {
		logger.info("Tx afterBegin ...");
	}
	
	@BeforeCompletion
	public void beforeCompletion() {
		logger.info("Tx beforeCompletion ...");
	}
	
	@AfterCompletion
	public void afterCompletition(boolean commited) {
		logger.info("Tx afterCompletition commited=" + commited);
	}
	
}
