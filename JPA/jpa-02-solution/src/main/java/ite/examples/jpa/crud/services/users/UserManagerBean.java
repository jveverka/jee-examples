package ite.examples.jpa.crud.services.users;

import java.util.logging.Logger;

import ite.examples.jpa.crud.entities.UserData;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 
 * @author Juraj Veverka
 * implements command/action part of the user service interface 
 * Class level defaults are: 
 * @TransactionManagement(TransactionManagementType.CONTAINER) 
 * @TransactionAttribute(TransactionAttributeType.REQUIRED)
 */
@Stateless
public class UserManagerBean {
	
	private static final Logger logger = Logger.getLogger(UserManagerBean.class.getName());
	private static final String USER_CREATED = "USER_CREATED";
	private static final String USER_UPDATED = "USER_UPDATED";
	private static final String USER_DELETED = "USER_DELETED";
	
	@PersistenceContext
    private EntityManager em;

	@Inject
	private UserMDBClientBean bdbClient;

	public void createUserAction(String firstName, String secondName, String email, String phone) {
		logger.info("createUserAction firstName=" + firstName + " secondName=" + secondName + " email=" + email + " phone=" + phone);
		UserData user = new UserData(firstName, secondName, email, phone);
		em.persist(user);
		bdbClient.sendTopicMessageAction(USER_CREATED);
	}
	
	public void updateUserAction(String id, String firstName, String secondName, String email, String phone) {
		logger.info("updateUserAction id=" + id + " firstName=" + firstName + " secondName=" + secondName + " email=" + email + " phone=" + phone);
		UserData user = em.find(UserData.class, id);
		if (user != null) {
			user.setFirstName(firstName);
			user.setSecondName(secondName);
			user.setEmail(email);
			user.setPhone(phone);
			bdbClient.sendTopicMessageAction(USER_UPDATED);
		} else {
			logger.severe("invalid user id: " + id);
		}
	}
	
	public void deleteUserAction(String id) {
		logger.info("deleteUserAction: " + id);
		UserData user = em.find(UserData.class, id);
		em.remove(user);
		bdbClient.sendTopicMessageAction(USER_DELETED);
	}
	
}
