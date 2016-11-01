package ite.examples.jpa.crud.services.users;

import java.util.logging.Logger;

import ite.examples.jpa.crud.entities.UserData;

import javax.ejb.Stateless;
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
	
	@PersistenceContext
	private EntityManager em;

	public void createUserAction(String firstName, String secondName, String email, String phone) {
		logger.info("createUserAction firstName=" + firstName + " secondName=" + secondName + " email=" + email + " phone=" + phone);
		UserData user = new UserData(firstName, secondName, email, phone);
		em.persist(user);
	}
	
	public void updateUserAction(Long id, String firstName, String secondName, String email, String phone) {
		logger.info("updateUserAction id=" + id + " firstName=" + firstName + " secondName=" + secondName + " email=" + email + " phone=" + phone);
		UserData user = em.find(UserData.class, id);
		if (user != null) {
			user.setFirstName(firstName);
			user.setSecondName(secondName);
			user.setEmail(email);
			user.setPhone(phone);
		} else {
			logger.severe("invalid user id: " + id);
		}
	}
	
	public void deleteUserAction(Long id) {
		logger.info("deleteUserAction: " + id);
		UserData user = em.find(UserData.class, id);
		em.remove(user);
	}
	
}
