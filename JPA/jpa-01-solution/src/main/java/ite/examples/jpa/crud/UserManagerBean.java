package ite.examples.jpa.crud;

import java.util.List;
import java.util.logging.Logger;

import ite.examples.jpa.crud.entities.UserData;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static ite.examples.jpa.crud.QueryRepository.Queries.USERS_GET_ALL;

/**
 * 
 * @author Juraj Veverka
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
	
	public void updateUserAction(String id, String firstName, String secondName, String email, String phone) {
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
	
	public void deleteUserAction(String id) {
		logger.info("deleteUserAction: " + id);
		UserData user = em.find(UserData.class, id);
		em.remove(user);
	}
	
}
