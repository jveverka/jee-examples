package ite.examples.template.services;

import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ite.examples.data.entities.Project;
import ite.examples.data.entities.User;

@Stateless
public class DataAccessService {

	private static final Logger logger = Logger.getLogger(DataAccessService.class.getName());
	
	@PersistenceContext
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User getUserById(Long id) {
		logger.info("getUserById:" + id);
		User user = em.find(User.class, id);
		user.setNick("from:dataAccessService");
		//user.setName(user.getName() + "x");
		logger.info("getUserById:" + id + " done.");
		return user;
	}
	
	public void persistUser(User user) {
		logger.info("persistUser:" + user.toString());
		em.persist(user);
	}

	//@Asynchronous
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Future<User> getUserByIdAsync(Long id) {
		logger.info("getUserByIdAsync:" + id);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		FutureUser upcommingUser = new FutureUser();
		User user = em.find(User.class, id);
		user.setNick("from:dataAccessService");
		upcommingUser.setUser(user);
		logger.info("getUserByIdAsync:" + id + " done.");
		return upcommingUser;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeOwner(Project project, User newOwner) {
		logger.info("changeOwner:" + project.toString() + " " + newOwner.toString());
		Project managedProject = em.merge(project);
		//newOwner = em.merge(newOwner);	
		managedProject.setOwner(newOwner);
		//em.detach(newOwner);
		//newOwner.addProject(managedProject);
		logger.info("changeOwner: done."); 
	}

}
