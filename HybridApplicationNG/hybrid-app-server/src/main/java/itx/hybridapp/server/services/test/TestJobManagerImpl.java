package itx.hybridapp.server.services.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

import itx.hybridapp.common.protocols.CommonAccessProtocol.EchoData;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestJobRequest;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestResult;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestResultListResponse;
import itx.hybridapp.server.services.dto.UserWsInfo;
import itx.hybridapp.server.services.useraccess.MessagePublisher;
import itx.hybridapp.server.services.useraccess.UserAccessService;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class TestJobManagerImpl implements TestJobManager {
	
	private static final Logger logger = Logger.getLogger(TestJobManagerImpl.class.getName());
	private static final String TEST_TOPIC_BASE = "/tests";
	
	private AtomicInteger jobCounter;
	private List<TestResult> results;
	private Map<Integer, TestJob> runningJobs;
	
	@Resource
    private ManagedExecutorService executorService;
	
	@Inject
	private UserAccessService uaService;

	@Inject 
	private MessagePublisher messagePublisher;

	@PostConstruct
	protected void init() {
		jobCounter = new AtomicInteger(0);
		results = new ArrayList<>();
		runningJobs = new ConcurrentHashMap<>();
	}
	
	@Override
	public TestResultListResponse getList() {
		logger.info("getList");
		TestResultListResponse.Builder trlrBuilder = TestResultListResponse.newBuilder();
		results.forEach(r -> { trlrBuilder.addTestResults(r); });
		return trlrBuilder.build();
	}

	@Override
	public void submitTestJob(TestJobRequest request) {
		logger.info("submiting test job");
		int jobId = jobCounter.incrementAndGet();
		try {
			UserWsInfo ui = uaService.getUserWsSessionInfo(request.getWsSessionId());
			TestJob testJob = new TestJob(jobId, ui, request, this);
			runningJobs.put(jobId, testJob);
			executorService.submit(testJob);
		} catch (LoginException e) {
			logger.severe("text job submit failed");
			TestResult result = TestResult.newBuilder()
					.setTestId(jobId)
					.setStarted(System.currentTimeMillis())
					.setPublishDuration(0)
					.setDuration(0)
					.setStatus("ERROR")
					.setProtocol("NA")
					.setClientId("NA")
					.setDescription("Test failed: unknown websocket session id")
					.setRequest(request)
					.build();
			onTestJobfinished(result);
		}
	}

	@Override
	public void onTestJobReply(EchoData echoData) {
		TestJob testJob = runningJobs.get(echoData.getJobId());
		if (testJob != null) {
			testJob.onEchoResponse(echoData);
		}
	}
	
	@Override
	public void publishTestResultListResponse() {
		logger.info("publishTestResultListResponse");
		WrapperMessage wm = WrapperMessage.newBuilder().setTestResultListResponse(getList()).build();
		messagePublisher.publishToTopic(TEST_TOPIC_BASE, wm);
	}

	public void sendMessage(String wsSessionId, WrapperMessage wrappedEcho) {
		messagePublisher.publishToWsSession(wsSessionId, wrappedEcho);
	}
	
	public void onTestJobfinished(TestResult result) {
		logger.info("onTestJobfinished: " + result.getTestId());
		results.add(result);
		runningJobs.remove(result.getTestId());
		publishTestResultListResponse();
	}

}
