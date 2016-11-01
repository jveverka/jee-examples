package ite.examples.ejb.security;

public interface BackendService {
	
	public String getPublicData(String name);
	
	public String getUserData(String name);

	public String getAdminData(String name);

}
