package itx.hybridapp.server.services.data;

import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestResponse;
import itx.hybridapp.server.services.useraccess.MessagePublisher;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DataTestServiceImpl implements DataTestService {
	
	private static final Logger logger = Logger.getLogger(DataTestServiceImpl.class.getName());

	@Inject 
	private MessagePublisher messagePublisher;

	@Override
	public String getData(String data) {
		String result = "response:" + data;
		logger.info("getData: " + result);
		return result;
	}

	@Override
	public void publishGetDataResponse(String wsSessionId, String data) {
		String response = getData(data);
		TestResponse testResponse = TestResponse.newBuilder().setData(response).build();
		WrapperMessage wmResponse = WrapperMessage.newBuilder().setTestResponse(testResponse).build();
		messagePublisher.publishToWsSession(wsSessionId, wmResponse);
	}

}
