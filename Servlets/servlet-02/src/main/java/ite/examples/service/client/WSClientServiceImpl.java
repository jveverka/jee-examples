package ite.examples.service.client;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

@Stateless
public class WSClientServiceImpl implements WSClientService {
	
	private final static Logger logger = Logger.getLogger(WSClientServiceImpl.class.getName());

	@Override
	public String sendGetRequest(String request) {
		logger.info("sendGetRequest: " + request);
		Client client = ClientBuilder.newClient();
		WebTarget getTarget = client.target("http://localhost:8080/servlet-02/ws/wsservice/get?message=" + request);
		Invocation getInvocation = getTarget.request().buildGet(); 
		String response = getInvocation.invoke(String.class);
		return response;
	}

	@Override
	public String sendPostRequest(String request) {
		logger.info("sendPostRequest: " + request);
		Client client = ClientBuilder.newClient();
		WebTarget postTarget = client.target("http://localhost:8080/servlet-02/ws/wsservice/post");
		Invocation postInvocation = postTarget.request().buildPost(Entity.entity(request,MediaType.TEXT_PLAIN)); 
		String response = postInvocation.invoke(String.class);
		return response;
	}
	
	

}
