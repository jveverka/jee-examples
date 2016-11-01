package ite.servlet.filterauth.service;

import javax.ejb.Local;

@Local
public interface DataService {
	
	public String getData(String request);

	public void setData(String response);

}
