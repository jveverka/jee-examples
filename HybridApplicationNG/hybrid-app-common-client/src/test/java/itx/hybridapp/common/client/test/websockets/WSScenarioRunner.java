package itx.hybridapp.common.client.test.websockets;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import itx.hybridapp.common.client.websocket.WSMessagePublisher;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;

public class WSScenarioRunner implements Runnable {
	
	private final static Logger logger = Logger.getLogger(WSScenarioRunner.class.getName());
	
	private WSSessionScenario scenario;
	private WSMessagePublisher messagePublisher;
	private boolean run;
	private CountDownLatch latch;
	private ScenarioStep currentStep;
	
	public WSScenarioRunner(WSSessionScenario scenario, WSMessagePublisher messagePublisher) {
		this.scenario = scenario;
		this.messagePublisher = messagePublisher;
		this.run = true;
	}

	@Override
	public void run() {
		logger.info("WSScenarioRunner: started");
		Iterator<ScenarioStep> steps = scenario.getSteps().iterator();
		int counter = 0;
		logger.info("WSScenarioRunner: steps: " + scenario.getSteps().size());
		while (steps.hasNext() && run) {
			logger.info("WSScenarioRunner: executing: [" + counter + "/" + scenario.getSteps().size() + "]"); 
			latch = new CountDownLatch(1);
			currentStep = steps.next();
			try {
				currentStep.setWasExecuted(true);
				messagePublisher.sendMessage(currentStep.getRequest());
				logger.info("WSScenarioRunner: waiting for response ...");
				latch.await(2000, TimeUnit.MILLISECONDS);
			} catch (IOException e) {
				currentStep.setActualException(e);
			} catch (InterruptedException e) {
				currentStep.setActualException(e);
			}
			logger.info("step done");
		}
		messagePublisher.close();
	}
	
	public void onMessage(WrapperMessage response) {
		currentStep.setActualResponse(response);
		logger.info("WSScenarioRunner: onMessage");
		latch.countDown();
	}
	
	public void setStop() {
		logger.info("WSScenarioRunner: stop !");
		run = false;
		latch.countDown();
	}

}
