package ite.examples.jpa.crud.ui;

import static ite.examples.jpa.crud.QueryRepository.Queries.USERS_GET_ALL;
import ite.examples.jpa.crud.entities.UserData;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * 
 * @author Juraj Veverka
 * @Transactional enables this CDI bean to utilize transactions outside EJB container (since JEE7). 
 * Default transaction attribute type @Transactional(TxType.REQUIRED)
 * 
 */

@Transactional
@SessionScoped
@Named("userviewda")
public class UserViewSCManager implements Serializable {

	private static final Logger logger = Logger.getLogger(UserViewClientBean.class.getName());
	
	@PersistenceContext
    private EntityManager em;
	
	private List<UserData> users;
	
	private String id;
	private String firstName;
	private String secondName;
	private String email;
	private String phone;	
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		id = null;
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
	
	public void setEditUserAction(String id) {
		this.id = id;
		for (UserData ud: users) {
			if (id.equals(ud.getId())) {
				firstName = ud.getFirstName();
				secondName = ud.getSecondName();
				phone = ud.getPhone();
				email = ud.getEmail();
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
		users = em.createNamedQuery(USERS_GET_ALL.name(), UserData.class).getResultList();
	}
	
	public void createUserAction() {
		logger.info("createUserAction ...");
		UserData user = new UserData(firstName, secondName, email, phone);
		em.persist(user);
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

	public void deleteUserAction(String id) {
		logger.info("deleteUserAction ...");
		UserData user = em.find(UserData.class, id);
		em.remove(user);
		reloadDataAction();
	}

	public void updateUserAction() {
		logger.info("updateUserAction ...");
		if (id == null) {
			logger.info("updateUserAction: ERROR, user Id is NULL !");
			return;
		}
		UserData user = em.find(UserData.class, id);
		if (user != null) {
			user.setFirstName(firstName);
			user.setSecondName(secondName);
			user.setPhone(phone);
			user.setEmail(email);
		} else {
			logger.severe("invalid user id: " + id);
		}
		cancelEditAction();
		reloadDataAction();
	}

	public List<UserData> getUsers() {
		return users;
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
