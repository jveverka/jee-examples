package ite.clusterdemo.services.wsrest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import ite.clusterdemo.services.dto.AsyncMessageResponse;
import ite.clusterdemo.services.dto.DataMessage;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class WsResponseCache {
	
	private static final Logger logger = Logger.getLogger(WsResponseCache.class.getName());
	
	private Map<String, AsyncMessageResponse> awaitingResponses;
	
	@PostConstruct
	private void init() {
		awaitingResponses = new HashMap<>();
	}
	
	public AsyncMessageResponse insertAwaitingResponse(DataMessage dataMessage) {
		logger.info("creating async message for: " + dataMessage.getMessageId());
		AsyncMessageResponse awaitingResponse = new AsyncMessageResponse(dataMessage);
		synchronized(awaitingResponses) {
			awaitingResponses.put(awaitingResponse.getRequestMessageId(), awaitingResponse);
		}
		return awaitingResponse;
	}
	
	public void setResponse(DataMessage dataMessage) {
		logger.info("setResponse for: " + dataMessage.getContextId());
		AsyncMessageResponse asyncResponse = null;
		synchronized(awaitingResponses) {
			asyncResponse = awaitingResponses.get(dataMessage.getContextId());
		}
		if (asyncResponse != null) { 
			asyncResponse.setResponseMessage(dataMessage);
		} else {
			logger.severe("no awaiting response found for " + dataMessage.getContextId());
		}
	}
	
	public void removeAwaitingResponse(String id) {
		logger.info("removing async message for: " + id);
		AsyncMessageResponse asyncResponse = null;
		synchronized(awaitingResponses) {
			asyncResponse = awaitingResponses.remove(id);
		}
		if (asyncResponse == null) {
			logger.severe("no awaiting response found for " + id);
		}
	}

}
