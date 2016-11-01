package ite.servlet.filterauth.service;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class DataServiceImpl implements DataService {
	
	private static final Logger logger = Logger.getLogger(DataServiceImpl.class.getName());
	
	private String response = "response";
	
	@Resource
	private SessionContext sessionContext;
	
	@Override
	public String getData(String request) {
		logger.info("getData: " + request + " by: " + sessionContext.getCallerPrincipal().getName());
		logger.info("is in role admin: " + sessionContext.isCallerInRole("admin"));
		logger.info("is in role root : " + sessionContext.isCallerInRole("root"));
		logger.info("is in role user : " + sessionContext.isCallerInRole("user"));
		logger.info("is in role demo : " + sessionContext.isCallerInRole("demo"));
		return response + ":" + request;
	}

	@Override
	public void setData(String response) {
		logger.info("setData: " + response + " by: " + sessionContext.getCallerPrincipal().getName());
		logger.info("is in role admin: " + sessionContext.isCallerInRole("admin"));
		logger.info("is in role root : " + sessionContext.isCallerInRole("root"));
		logger.info("is in role user : " + sessionContext.isCallerInRole("user"));
		logger.info("is in role demo : " + sessionContext.isCallerInRole("demo"));
		this.response = response;
	}

}
