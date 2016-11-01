package ite.examples.jpa.crud.ui;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import ite.examples.jpa.crud.entities.UserData;
import ite.examples.jpa.crud.services.users.UserDataService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("userview")
public class UserViewClientBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserViewClientBean.class.getName());
	private List<UserData> users;
	
	private Long id;
	private String firstName;
	private String secondName;
	private String email;
	private String phone;
	
	@Inject 
	private UserDataService userDataService;

	@PostConstruct
	private void init() {
		logger.info("init ...");
		id = null;
		reloadDataAction();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isEditMode() {
		return id != null;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEditUserAction(Long id) {
		this.id = id;
		for (UserData ud: users) {
			if (id.equals(ud.getId())) {
				firstName = ud.getFirstName();
				secondName = ud.getSecondName();
				email = ud.getEmail();
				phone = ud.getPhone();
				break;
			}
		}
	}

	public int getUserCount() {
		if (users != null) {
			return users.size();
		}
		return 0;
	}

	public void reloadDataAction() {
		logger.info("reloadDataAction ...");
		users = userDataService.getAllUsers();
	}
	
	public void createUserAction() {
		logger.info("createUserAction ...");
		userDataService.createUserAction(firstName, secondName, email, phone);
		cancelEditAction();
		reloadDataAction();
	}

	public void deleteUserAction(Long id) {
		logger.info("deleteUserAction ...");
		userDataService.deleteUserAction(id);
		reloadDataAction();
	}

	public void updateUserAction() {
		logger.info("updateUserAction ...");
		if (id == null) {
			logger.info("updateUserAction: ERROR, user Id is NULL !");
			return;
		}
		userDataService.updateUserAction(id, firstName, secondName, email, phone);
		cancelEditAction();
		reloadDataAction();
	}
	
	public void cancelEditAction() {
		id = null;
		firstName = "";
		secondName = "";
		phone = "";
		email = "";
	}

	public List<UserData> getUsers() {
		return users;
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
