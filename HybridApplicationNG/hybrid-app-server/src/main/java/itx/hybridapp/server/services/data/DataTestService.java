package itx.hybridapp.server.services.data;

import javax.ejb.Local;

@Local
public interface DataTestService {
	
	public String getData(String data);
	
	public void publishGetDataResponse(String wsSessionId, String data);

}
