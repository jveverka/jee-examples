package ite.examples.ejbclient.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ite.examples.ejbservice.services.DataServiceRemote;


@SessionScoped
@Named("dataClient")
public class DataBean implements Serializable {

	private static final Logger logger = Logger.getLogger(DataBean.class.getName());
	private String data;
	private String result;
	
	//@Resource(lookup = "java:global/ejb-service/SingletonDataService!ite.examples.ejbservice.services.DataServiceRemote")
	private DataServiceRemote ds;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		data = "";
		result = "";
		try {
			ds = (DataServiceRemote) new InitialContext().lookup("java:global/ejb-service/SingletonDataService!ite.examples.ejbservice.services.DataServiceRemote");
			logger.info("DataServiceRemote resolved OK");
		} catch (NamingException e) {
			logger.severe("Failed to resolve DataServiceRemote !");
		}
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void doSubmitAction() {
		logger.info("doSubmitAction");
		result = ds.getDataRemote(data);
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
