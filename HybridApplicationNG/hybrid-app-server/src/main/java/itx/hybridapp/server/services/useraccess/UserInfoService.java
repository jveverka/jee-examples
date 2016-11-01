package itx.hybridapp.server.services.useraccess;

import javax.ejb.Local;

import itx.hybridapp.common.protocols.UserAccessProtocol.UserInfoData;

@Local
public interface UserInfoService {
	
	public UserInfoData getUserData();

}
