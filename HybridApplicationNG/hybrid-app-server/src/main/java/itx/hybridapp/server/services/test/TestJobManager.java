package itx.hybridapp.server.services.test;

import javax.ejb.Local;

import itx.hybridapp.common.protocols.CommonAccessProtocol.EchoData;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestJobRequest;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestResultListResponse;

@Local
public interface TestJobManager {
	
	public TestResultListResponse getList();
	
	public void publishTestResultListResponse();
	
	public void submitTestJob(TestJobRequest request);
	
	public void onTestJobReply(EchoData echoData);

}
