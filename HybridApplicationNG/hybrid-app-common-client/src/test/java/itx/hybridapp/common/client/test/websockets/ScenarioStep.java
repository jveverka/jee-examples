package itx.hybridapp.common.client.test.websockets;

import java.util.logging.Logger;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;

public class ScenarioStep {
	
	private final static Logger logger = Logger.getLogger(ScenarioStep.class.getName());
	
	private WrapperMessage request;
	private WrapperMessage expectedResponse;
	private boolean expectedException;
	private WrapperMessage actualResponse;
	private Throwable actualException;
	private boolean wasExecuted;
	
	public ScenarioStep(WrapperMessage request, WrapperMessage expectedResponse, boolean expectedException) {
		this.request = request;
		this.expectedResponse = expectedResponse;
		this.expectedException = expectedException;
		this.wasExecuted = false;
	}

	public WrapperMessage getActualResponse() {
		return actualResponse;
	}

	public boolean isExpectedException() {
		return expectedException;
	}

	public void setActualResponse(WrapperMessage actualResponse) {
		logger.info("setActualResponse");
		this.actualResponse = actualResponse;
	}

	public void setActualException(Throwable actualException) {
		logger.info("setActualException");
		this.actualException = actualException;
	}

	public WrapperMessage getRequest() {
		return request;
	}

	public WrapperMessage getExpectedResponse() {
		return expectedResponse;
	}

	public Throwable getActualException() {
		return actualException;
	}

	public boolean isWasExecuted() {
		return wasExecuted;
	}

	public void setWasExecuted(boolean wasExecuted) {
		this.wasExecuted = wasExecuted;
	}

}
