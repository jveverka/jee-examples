package ite.online.training;

import javax.ejb.Local;

@Local
public interface UserVerificationService {
	
	public boolean isUserAuthorized(String userName, String password);

	public boolean isAdminAuthorized(String userName, String password);

	public boolean changeUserPassword(String userName, String oldPassword, String newPassword);

	public boolean changeAdminPassword(String userName, String oldPassword, String newPassword);

	public String createUser(String userName);

	public boolean confirmUser(String userName, String token);

	public boolean deleteUser(String userName, String password);

}
