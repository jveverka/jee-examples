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

import ite.examples.data.entities.Program;

public class ProgramListener {

	private static final Logger logger = Logger.getLogger(ProgramListener.class.getName());
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
	@PrePersist
	public void prePersist(Program program) {
		logger.info("prePersist: " + program.toString());
	}
	
	@PostPersist 
	public void postPersist(Program program) {
		logger.info("postPersist: " + program.toString());
	}
	
	@PreRemove
	public void preRemove(Program program) {
		logger.info("preRemove: " + program.toString());
	}
	
	@PostRemove
	public void postRemove(Program program) {
		logger.info("postRemove: " + program.toString());
	}
	
	@PreUpdate
	public void preUpdate(Program program) {
		logger.info("preUpdate: " + program.toString());
	}
	
	
	@PostUpdate 
	public void postUpdate(Program program) {
		logger.info("postUpdate: " + program.toString());
	}
	
	@PostLoad
	public void postLoad(Program program) {
		logger.info("postLoad: " + program.toString());
	}
	
}
