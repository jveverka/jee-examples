package ite.examples.jpa.crud.services;

import ite.examples.jpa.crud.entities.UserData;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class DataInItializator {
	
	private static final Logger logger = Logger.getLogger(DataInItializator.class.getName());
	
	@PersistenceContext
    private EntityManager em;
	
	@PostConstruct
	private void init() {
		logger.info("init ... " + Thread.currentThread().getName());
		//List<UserData> users = DataUtils.createEntities01();
		//for (UserData user: users) {
		//	logger.info(DataUtils.userDataToString(user));
		//	em.persist(user);
		//}
	}

}
