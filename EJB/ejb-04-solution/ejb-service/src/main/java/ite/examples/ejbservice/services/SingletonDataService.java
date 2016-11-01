package ite.examples.ejbservice.services;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import ite.examples.ejbservice.services.DataServiceLocal;

@Singleton
@Startup
@ApplicationScoped
public class SingletonDataService implements DataServiceRemote, DataServiceLocal {
	
	private static final Logger logger = Logger.getLogger(SingletonDataService.class.getName());
	
	private int counter;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		counter = 0;
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

	private String getData(String request) {
		String reply = "relpy[" + (counter++) + "]: " + request;
		logger.info("getData: " + request + " / " + reply);
		return reply;
	}

	@Override
	public String getDataLocal(String request) {
		logger.info("getDataLocal");
		return getData(request);
	}

	@Override
	public String getDataRemote(String request) {
		logger.info("getDataRemote");
		return getData(request);
	}
	
}
