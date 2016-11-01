package itx.hybridapp.common.client.rest;

import itx.hybridapp.common.client.ClientServiceException;

public interface DataServiceClient {
	
	public static DataServiceClient buildClient(String baseURL, String mediaType) {
		return new DataServiceClientImpl(baseURL, mediaType);
	}
	
	public void setHttpSessionId(String sessionId);
	
	public String getData(String data) throws ClientServiceException;

}
