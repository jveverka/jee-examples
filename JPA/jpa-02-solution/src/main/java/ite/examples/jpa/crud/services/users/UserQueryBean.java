package ite.examples.jpa.crud.services.users;

import static ite.examples.jpa.crud.services.users.UserQueryRepository.Queries.USERS_GET_ALL;
import ite.examples.jpa.crud.entities.UserData;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * implements query/read part of the interface user service
 * @author Juraj Veverka
 * Class level defaults are: 
 * @TransactionManagement(TransactionManagementType.CONTAINER) 
 * @TransactionAttribute(TransactionAttributeType.REQUIRED)
 */

@Stateless
public class UserQueryBean {

	private static final Logger logger = Logger.getLogger(UserManagerBean.class.getName());
	
	@PersistenceContext
    private EntityManager em;
	
	public List<UserData> getAllUsers() {
		logger.info("getAllUsers ...");
		List<UserData> users = em.createNamedQuery(USERS_GET_ALL.name(), UserData.class).getResultList();
		return users;
	}

	public UserData getUserAction(String id) {
		logger.info("getUserAction id=" + id);
		UserData user = em.find(UserData.class, id);
		return user;
	}

}
