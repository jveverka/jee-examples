package ite.examples.jpa.crud.services.users;

import ite.examples.jpa.crud.entities.UserData;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Implements complete CRUD interface
 * with event notifications
 * @author Juraj Veverka
 * Class level defaults are: 
 * @TransactionManagement(TransactionManagementType.CONTAINER) 
 * @TransactionAttribute(TransactionAttributeType.REQUIRED)
 */
@Stateless
public class UserDataService {
	
	@Inject
	private UserManagerBean userManager;
	
	@Inject
	private UserQueryBean userQueryBean;
	
	public List<UserData> getAllUsers() {
		return userQueryBean.getAllUsers();
	}
	
	public UserData getUserAction(String id) {
		return userQueryBean.getUserAction(id);
	}
	
	public void createUserAction(String firstName, String secondName, String email, String phone) {
		userManager.createUserAction(firstName, secondName, email, phone);
	}
	
	public void updateUserAction(Long id, String firstName, String secondName, String email, String phone) {
		userManager.updateUserAction(id, firstName, secondName, email, phone);
	}
	
	public void deleteUserAction(Long id) {
		userManager.deleteUserAction(id);
	}

}
