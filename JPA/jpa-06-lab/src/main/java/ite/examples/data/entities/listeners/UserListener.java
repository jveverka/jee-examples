package ite.examples.data.entities.listeners;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import ite.examples.data.entities.User;

public class UserListener {

	private static final Logger logger = Logger.getLogger(UserListener.class.getName());
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

	@PrePersist
	public void prePersist(User user) {
		logger.info("prePersist: " + user.toString());
		user.setNick("from:UserListener");
	}
	
	@PostPersist 
	public void postPersist(User user) {
		logger.info("postPersist: " + user.toString());
	}
	
	@PreRemove
	public void preRemove(User user) {
		logger.info("preRemove: " + user.toString());
	}
	
	@PostRemove
	public void postRemove(User user) {
		logger.info("postRemove: " + user.toString());
	}
	
	@PreUpdate
	public void preUpdate(User user) {
		logger.info("preUpdate: " + user.toString());
	}
	
	
	@PostUpdate 
	public void postUpdate(User user) {
		logger.info("postUpdate: " + user.toString());
	}
	
	@PostLoad
	public void postLoad(User user) {
		logger.info("postLoad: " + user.toString());
	}
	
}
