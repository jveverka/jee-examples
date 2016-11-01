package ite.examples.service.client;

import javax.ejb.Local;

@Local
public interface WSClientService {
	
	public String sendGetRequest(String request);

	public String sendPostRequest(String request);

}
