package ite.examples.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.examples.service.client.WSClientService;

@SessionScoped
@Named("wsController")
public class WSControllerBean implements Serializable {
	
	private final static Logger logger = Logger.getLogger(WSControllerBean.class.getName());

	private String message;
	private String response;
	
	@Inject
	private WSClientService wsClient;

	public String getResponse() {
		return response;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void doGetAction() {
		logger.info("doGetAction ...");
		response = wsClient.sendGetRequest(message);
	}

	public void doPostAction() {
		logger.info("doPostAction ...");
		response = wsClient.sendPostRequest(message);
	}

}
