package itx.hybridapp.common.client.test.websockets;

import itx.hybridapp.common.client.websocket.WSEventListener;
import itx.hybridapp.common.client.websocket.WSMessagePublisher;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;

public class WSTestListener implements WSEventListener {
	
	private WSSessionScenario scenario;
	private WSMessagePublisher messagePublisher;
	private WSScenarioRunner runner;
	
	public WSTestListener(WSSessionScenario scenario) {
		this.scenario = scenario;
	}

	@Override
	public void onInit(WSMessagePublisher messagePublisher) {
		this.messagePublisher = messagePublisher;
	}

	@Override
	public void onSessionCreated(String wsSessionId) {
		runner = new WSScenarioRunner(scenario, messagePublisher);
		Thread runnerThread = new Thread(runner);
		runnerThread.start();
	}

	@Override
	public void onMessage(WrapperMessage wm) {
		runner.onMessage(wm);
	}

	@Override
	public void onSessionClosed() {
	}

	@Override
	public void onSessionError() {
		runner.setStop();
	}

	@Override
	public void onShutdown() {
	}

}
