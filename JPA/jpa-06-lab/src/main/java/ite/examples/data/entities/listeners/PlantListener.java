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

import ite.examples.data.entities.Plant;

public class PlantListener {

	private static final Logger logger = Logger.getLogger(PlantListener.class.getName());
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
	@PrePersist
	public void prePersist(Plant plant) {
		logger.info("prePersist: " + plant.toString());
	}
	
	@PostPersist 
	public void postPersist(Plant plant) {
		logger.info("postPersist: " + plant.toString());
	}
	
	@PreRemove
	public void preRemove(Plant plant) {
		logger.info("preRemove: " + plant.toString());
	}
	
	@PostRemove
	public void postRemove(Plant plant) {
		logger.info("postRemove: " + plant.toString());
	}
	
	@PreUpdate
	public void preUpdate(Plant plant) {
		logger.info("preUpdate: " + plant.toString());
	}
	
	
	@PostUpdate 
	public void postUpdate(Plant plant) {
		logger.info("postUpdate: " + plant.toString());
	}
	
	@PostLoad
	public void postLoad(Plant plant) {
		logger.info("postLoad: " + plant.toString());
	}
	
}
