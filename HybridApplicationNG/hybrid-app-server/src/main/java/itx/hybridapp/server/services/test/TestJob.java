package itx.hybridapp.server.services.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import itx.hybridapp.common.protocols.CommonAccessProtocol.EchoData;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestJobRequest;
import itx.hybridapp.common.protocols.TestServiceProtocol.TestResult;
import itx.hybridapp.server.services.dto.UserWsInfo;

public class TestJob implements Callable<TestResult> {
	
	private static final Logger logger = Logger.getLogger(TestJob.class.getName());
	
	private static final String OK = "OK";
	private static final String FAILED = "FAILED";
	private static final String TIMEOUT = "TIMEOUT";
	
	private int jobId;
	private TestJobRequest request;
	private TestJobManagerImpl testJobManager;
	private long started;
	private long publishDuration;
	private long duration;
	private CountDownLatch counter;
	private int received;
	private UserWsInfo ui;
	private boolean[] results;
	private final String payloadData;
	
	public TestJob(int jobId, UserWsInfo ui, TestJobRequest request, TestJobManagerImpl testJobManager) {
		this.jobId = jobId;
		this.request = request;
		this.testJobManager = testJobManager;
		this.counter = new CountDownLatch(request.getRepeat());
		this.received = 0;
		this.ui = ui;
		this.results = new boolean[request.getRepeat()];
		this.payloadData = request.getPayload();
	}

	@Override
	public TestResult call() throws Exception {
		try {
			EchoData.Builder echoDataTemplateBuilder = EchoData.newBuilder()
				.setJobId(jobId)
				.setPayload(request.getPayload())
				.setOrdinal(0);
			if (request.getStructuredPayloadSize() > 0) {
				echoDataTemplateBuilder.addAllStructuredPayload(TestUtils.getStructuredPayload(request.getStructuredPayloadSize()));
			}
			EchoData echoDataTemplate = echoDataTemplateBuilder.build();
			
			started = System.currentTimeMillis();
			logger.info("Test Job started: " + jobId);
			logger.info("repeat : " + request.getRepeat());
			logger.info("payload: " + request.getPayload());
			logger.info("payload: " + request.getStructuredPayloadSize());
			logger.info("sending echo requests ...");
			for (int i=0; i<request.getRepeat(); i++) {
				EchoData echo = EchoData.newBuilder(echoDataTemplate).setOrdinal(i).build();
				WrapperMessage wm = WrapperMessage.newBuilder().setEchoData(echo).build();
				testJobManager.sendMessage(request.getWsSessionId(), wm);
				results[i] = false;
			}
			publishDuration = System.currentTimeMillis() - started;
			logger.info("All messages send! " + request.getRepeat());
			long awaitForMs = getAwaitPeriod(request.getRepeat(), request.getStructuredPayloadSize());
			logger.info("waiting for echo responses for: " + awaitForMs + " ms");
			boolean isInTime = counter.await(awaitForMs, TimeUnit.MILLISECONDS);
			logger.info("all responses arrived: " + isInTime);
			duration = System.currentTimeMillis() - started;
			logger.info("Test Job finished: " + jobId + " in " + duration + "ms");
			logger.info("repeat : " + request.getRepeat());
			logger.info("payload: " + request.getPayload());
			logger.info("payload: " + request.getStructuredPayloadSize());
			logger.info("checking results ...");
			boolean allOK = true;
			for (int i=0; i<results.length; i++) {
				if (!results[i]) {
					logger.info("result check failed for: " + i);
					allOK = false;
					//break;
				}
			}
			
			logger.info("isInTime: " + isInTime);
			logger.info("allOK   : " + allOK);
			
			String status = OK;
			String description = "Test job finished OK.";
			if (!isInTime) {
			    status = TIMEOUT;
			    description = "Test job timeout error.";
			} else if (!allOK) {
				status = FAILED;
				description = "Some echo responses missing or invalid data detected.";
			}
			
			TestResult result = TestResult.newBuilder()
				.setTestId(jobId)
				.setStarted(started)
				.setPublishDuration(publishDuration)
				.setDuration(duration)
				.setStatus(status)
				.setClientId(ui.getUserName())
				.setProtocol(ui.getProtocol())
				.setDescription(description)
				.setRequest(request)
				.build();
			testJobManager.onTestJobfinished(result);
			return result;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Test Job FAILED: ", e);
			TestResult result = TestResult.newBuilder()
					.setTestId(jobId)
					.setStarted(started)
					.setPublishDuration(publishDuration)
					.setDuration(duration)
					.setStatus(FAILED)
					.setProtocol(ui.getProtocol())
					.setDescription("Test failed:" + e.getMessage())
					.setRequest(request)
					.build();
			testJobManager.onTestJobfinished(result);
			throw e;
		}
	}
	
	public void onEchoResponse(EchoData echo) {
		received++;
		results[echo.getOrdinal()] = payloadData.equals(echo.getPayload());
		counter.countDown();
		logger.fine("onEchoResponse: " + received);
	}
	
	private long getAwaitPeriod(long repeat, int structSize) {
		long result = 4*repeat*(4*structSize);
		if (result > 10000) {
			return result;
		}
		return 10000;
	}

}
