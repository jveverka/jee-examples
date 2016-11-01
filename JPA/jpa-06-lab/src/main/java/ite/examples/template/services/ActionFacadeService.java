package ite.examples.template.services;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import ite.examples.data.entities.User;
import ite.examples.template.services.jdbc.DBService;
import ite.examples.template.services.jdbc.UserDTO;

@Stateless
public class ActionFacadeService {
	
	private static final Logger logger = Logger.getLogger(ActionFacadeService.class.getName());

	@Inject
	private DBService dbService;
	
	@Inject
	private DataAccessService das;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User getUserById(Long id) {
		logger.info("getUserById: " + id);
		User user = das.getUserById(id);
		user.setNick("from:ActionFacadeService");
		logger.info("getUserById: " + id + " done.");
		return user;
	}
	
	public void createUser() {
		User user = new User("John");
		user.setNick("doe");
		das.persistUser(user);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User getUserByIdAsync(Long id) {
		logger.info("getUserByIdAsync: " + id);
		try {
			Future<User> upcommingUser = das.getUserByIdAsync(id);
			User user = upcommingUser.get();
			user.setNick("from:ActionFacadeService");
			logger.info("getUserByIdAsync: " + id + " done.");
			return user;
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		return null;
	}
	
	public List<UserDTO> getUsersAction() {
		return dbService.getUsersAction();
	}
}
