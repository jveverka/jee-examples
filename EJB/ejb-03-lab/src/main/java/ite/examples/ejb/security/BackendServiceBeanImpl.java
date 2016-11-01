package ite.examples.ejb.security;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

import java.util.logging.Logger;

@Stateful
@Remote(BackendServiceRemote.class)
@Local(BackendService.class)
public class BackendServiceBeanImpl implements BackendService {

	private static final Logger logger = Logger.getLogger(BackendServiceBeanImpl.class.getName());
	
	@Resource
	private SessionContext sctx;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

	@Override
	@PermitAll
	public String getPublicData(String name) {
		logger.info("getPublicData for: " + name);
		return "Public data for user: " + name;
	}

	@Override
	//@RolesAllowed("admins")
	@RolesAllowed({"users", "admins"})
	public String getUserData(String name) {
		logger.info("getUserData for: " + name);
		return "User data for user: " + name;
	}

	@Override
	@RolesAllowed("admins")
	public String getAdminData(String name) {
		logger.info("getAdminData for: " + name);
		return "Admin data for user: " + name;
	}
	
}
