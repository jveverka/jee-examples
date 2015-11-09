package ite.clusterdemo.services.dto;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncMessageResponse implements Future<DataMessage> {
	
	private DataMessage requestMessage;
	private DataMessage responseMessage;
	
	public AsyncMessageResponse(DataMessage requestMessage) {
		this.requestMessage = requestMessage;
		this.responseMessage = null;
	}
	
	public String getRequestMessageId() {
		return requestMessage.getMessageId();
	}
	
	public void setResponseMessage(DataMessage responseMessage) {
		synchronized(this) {
			this.responseMessage = responseMessage;
			this.notifyAll();
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return responseMessage != null;
	}

	@Override
	public DataMessage get() throws InterruptedException, ExecutionException {
		synchronized(this) {
			this.wait();
		}
		return responseMessage;
	}

	@Override
	public DataMessage get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		synchronized(this) {
			this.wait(unit.toMillis(timeout));
		}
		return responseMessage;
	}

}
