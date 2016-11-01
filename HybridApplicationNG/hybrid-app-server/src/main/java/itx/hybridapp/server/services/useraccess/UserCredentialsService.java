package itx.hybridapp.server.services.useraccess;

import java.util.List;

import javax.ejb.Local;
import javax.security.auth.login.LoginException;

@Local
public interface UserCredentialsService {
	
	/**
	 * verify user's credentials
	 * @param userName
	 *   unique user id used as key for lookup
	 * @param password
	 *   user's plain text password
	 * @return
	 *   list of user's roles
	 * @throws LoginException
	 *   thrown in case user's credentials are not valid
	 */
	public List<String> verifyCredentials(String userName, String password) throws LoginException;

}
