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

import ite.examples.data.entities.Assignment;

public class AssignmentListener {
	
	private static final Logger logger = Logger.getLogger(AssignmentListener.class.getName());
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
	@PrePersist
	public void prePersist(Assignment assignment) {
		logger.info("prePersist: " + assignment.toString());
	}
	
	@PostPersist 
	public void postPersist(Assignment assignment) {
		logger.info("postPersist: " + assignment.toString());
	}
	
	@PreRemove
	public void preRemove(Assignment assignment) {
		logger.info("preRemove: " + assignment.toString());
	}
	
	@PostRemove
	public void postRemove(Assignment assignment) {
		logger.info("postRemove: " + assignment.toString());
	}
	
	@PreUpdate
	public void preUpdate(Assignment assignment) {
		logger.info("preUpdate: " + assignment.toString());
	}
	
	
	@PostUpdate 
	public void postUpdate(Assignment assignment) {
		logger.info("postUpdate: " + assignment.toString());
	}
	
	@PostLoad
	public void postLoad(Assignment assignment) {
		logger.info("postLoad: " + assignment.toString());
	}

}
