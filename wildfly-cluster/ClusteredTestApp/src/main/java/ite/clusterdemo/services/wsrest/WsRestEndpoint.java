package ite.clusterdemo.services.wsrest;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ite.clusterdemo.services.ConfigData;
import ite.clusterdemo.services.dto.AsyncMessageResponse;
import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.dto.DataMessageType;
import ite.clusterdemo.services.dto.MessageUtils;
import ite.clusterdemo.services.dto.SessionInfo;
import ite.clusterdemo.services.topic.TopicPublisher;
import ite.clusterdemo.services.websocket.WsClientGlobalRegistry;

@Path("/wsrest")
public class WsRestEndpoint {

	private final static Logger logger = Logger.getLogger(WsRestEndpoint.class.getName());
	
	@Inject
	private ConfigData configData;
	
	@Inject
	private TopicPublisher publisher;
	
	@Inject
	private WsResponseCache responseCache;
	
	@Inject
	private WsClientGlobalRegistry globalClientRegistry;

	@GET
	@Path("senddatamessage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String sendDataMessage(@QueryParam("userId")String userId, 
    		@QueryParam("sessionId")String sessionId, 
    		@QueryParam("messageId")String messageId, 
    		@QueryParam("contextId")String contextId, 
    		@QueryParam("message")String message) {
		logger.info("sendDataMessage: " + userId + ":" + sessionId + ":" + messageId + ":" + contextId + ":" + message);
		DataMessage dataMessage = new DataMessage(userId, sessionId, messageId, contextId, 
				DataMessageType.dataMessage, "OK", MessageUtils.createSimpleMessagePayload(message));
		publisher.publishMessage(dataMessage);
        return MessageUtils.createOKResponse("message accepted");
    }

	@GET
	@Path("senddatamessagesync")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String sendDataMessageSync(@QueryParam("userId")String userId, 
    		@QueryParam("sessionId")String sessionId, 
    		@QueryParam("messageId")String messageId, 
    		@QueryParam("contextId")String contextId, 
    		@QueryParam("message")String message) {
		logger.info("sendDataMessageSync: " + userId + ":" + sessionId + ":" + messageId + ":" + contextId + ":" + message);
		long startTime = System.currentTimeMillis();
		if (!globalClientRegistry.isSessionActive(sessionId)) {
			return MessageUtils.createErrorResponse("target session is not active");
		}
		DataMessage dataMessage = new DataMessage(userId, sessionId, messageId, contextId, 
				DataMessageType.dataMessage, "OK", MessageUtils.createSimpleMessagePayload(message));
		AsyncMessageResponse awaitingResponse = responseCache.insertAwaitingResponse(dataMessage);
		String responseString = MessageUtils.createErrorResponse("failed to send message");
		long afterMesagePublish = 0;
		try {
			publisher.publishMessage(dataMessage);
			afterMesagePublish = System.currentTimeMillis();
			DataMessage response = awaitingResponse.get(5000, TimeUnit.MILLISECONDS);
			if (response != null) {
				logger.info("got response: " + response.toString());
				responseString = MessageUtils.encode(response);
			} else {
				responseString = MessageUtils.createErrorResponse("message delivery timeout");
				logger.severe("sendDataMessageSync: message delivery timeout !"); 
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.severe("sendDataMessageSync: " + e.getMessage());
		}
		long afterResponseReceived = System.currentTimeMillis();
		responseCache.removeAwaitingResponse(awaitingResponse.getRequestMessageId());
		long doneTimestamp = System.currentTimeMillis();
		logger.info("wait for response  : " + (afterResponseReceived - afterMesagePublish) + "ms");
		logger.info("total duration     : " + (doneTimestamp - startTime) + "ms");
        return responseString;
    }

	@GET
	@Path("getsessions")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSessions() {
		List<SessionInfo> sessions = globalClientRegistry.getActiveSessions();
		return MessageUtils.sessionInfoListToJson(sessions);
    }

	@GET
	@Path("getserverinfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getServerInfo() {
		return MessageUtils.createServerInfoResponse(configData.getHostname(), configData.getServerId());
    }

}
