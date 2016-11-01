package ite.examples.template.services;

import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ite.examples.data.entities.Project;
import ite.examples.data.entities.User;

@Stateless
public class ProjectService {

	private static final Logger logger = Logger.getLogger(ProjectService.class.getName());

	@PersistenceContext
	private EntityManager em;

	@Inject
	private DataAccessService das;

	public void changeOwner(Long projectId, Long newOwnerId) {
		Project project = em.find(Project.class, projectId);
		User newOwner = em.find(User.class, newOwnerId);
		das.changeOwner(project, newOwner);
	}

	public void createProject(String name) {
		logger.info("createProject: " + name);
		Project project = new Project(name, null);
		em.persist(project);
	}

	@Asynchronous
	public void onLoadAsync() {
		logger.info("onLoadAsync: ");
	}

	@Asynchronous
	public void onPostPersistAsync() {
		logger.info("onPostPersistAsync: ");
	}

}
