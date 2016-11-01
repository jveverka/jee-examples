package ite.examples.ejbservice.services;

import javax.ejb.Local;

@Local
public interface DataServiceLocal {
	
	public String getDataLocal(String request);

}
