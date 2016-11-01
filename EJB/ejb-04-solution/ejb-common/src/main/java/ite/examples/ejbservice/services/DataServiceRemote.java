package ite.examples.ejbservice.services;

import javax.ejb.Remote;

@Remote
public interface DataServiceRemote {
	
	public String getDataRemote(String request);

}
