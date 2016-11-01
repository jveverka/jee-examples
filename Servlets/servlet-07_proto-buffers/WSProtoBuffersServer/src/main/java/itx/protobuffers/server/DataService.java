package itx.protobuffers.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import itx.protobuffers.common.UsersProtoc.User;
import itx.protobuffers.common.UsersProtoc.UserList;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class DataService {
	
	private static final Logger logger = Logger.getLogger(DataService.class.getName());
	
	Map<String, User> users;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		users = new HashMap<>();
		
		User user; 
		user = User.newBuilder().setUserName("john").setEmail("john@gmail.com").setNote("John Doe").build();
		users.put("john", user);
		user = User.newBuilder().setUserName("jane").setEmail("jane@gmail.com").setNote("Jane Doe").build();
		users.put("jane", user);
		user = User.newBuilder().setUserName("bob").setEmail("bob@gmail.com").setNote("Bob Doe").build();
		users.put("bob", user);
		user = User.newBuilder().setUserName("alice").setEmail("alice@gmail.com").setNote("Alice Doe").build();
		users.put("alice", user);
	}
	
	public User getUserByName(String userName) {
		return users.get(userName);
	}
	
	public UserList getAllUsers() {
		return UserList.newBuilder().addAllUser(users.values()).build();
	}

}
