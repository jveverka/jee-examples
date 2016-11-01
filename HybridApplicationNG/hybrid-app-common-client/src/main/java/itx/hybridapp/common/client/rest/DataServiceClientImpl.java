package itx.hybridapp.common.client.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import itx.hybridapp.common.client.ClientServiceException;
import itx.hybridapp.common.client.ClientUtils;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestRequest;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestResponse;

public class DataServiceClientImpl implements DataServiceClient {
	
	private String baseURL;
	private String mediaType;
	private NewCookie jsessionId;
	private Client client;
	
	protected DataServiceClientImpl(String baseURL, String mediaType) {
		this.baseURL = baseURL;
		this.mediaType = mediaType;
		client = ClientUtils.createHttpClient();
	}

	@Override
	public String getData(String data) throws ClientServiceException {
		try {
			WebTarget target = client.target(baseURL + ClientUtils.TESTDATA_URL);
			TestRequest testRequest = TestRequest.newBuilder().setData(data).build();
			Builder builder = target.request();
			if (jsessionId != null) {
				builder.cookie(jsessionId);
			}
			Response response = builder.post(Entity.entity(testRequest, mediaType));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				throw new ClientServiceException("invalid http status " + response.getStatus());
			}
			TestResponse testResponse = response.readEntity(TestResponse.class);
			return testResponse.getData();
		} catch (Exception e) {
			throw new ClientServiceException(e);
		}
	}

	@Override
	public void setHttpSessionId(String sessionId) {
		jsessionId = new NewCookie(ClientUtils.JSESSIONID, sessionId);
	}

}
