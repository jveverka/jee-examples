package ite.clusterdemo.services.dto;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import ite.clusterdemo.services.topic.SystemEvent;

public final class MessageUtils {
	
	public static final String sourceId = "sourceId";
	public static final String targetId = "targetId";
	public static final String messageId = "messageId";
	public static final String contextId = "contextId";
	public static final String messageType = "messageType";
	public static final String returnCode = "returnCode";
	public static final String payloadData = "payloadData";
	
	public static final String OK = "OK"; 
	public static final String ERROR = "ERROR"; 
			
	public static DataMessage decode(String stringMessage) {
		JsonReader reader = Json.createReader(new StringReader(stringMessage));
		JsonObject obj = reader.readObject();
		DataMessage request = new DataMessage();
		request.setSourceId(obj.getString(sourceId));
		request.setTargetId(obj.getString(targetId));
		request.setMessageId(obj.getString(messageId));
		request.setContextId(obj.getString(contextId));
		request.setMessageType(DataMessageType.valueOf(obj.getString(messageType)));
		request.setReturnCode(obj.getString(returnCode));
		request.setPayloadData(obj.getString(payloadData));
		return request;
	}

	public static String encode(DataMessage message) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder
			.add(sourceId, (message.getSourceId() != null) ? message.getSourceId() : "")
			.add(targetId, (message.getTargetId() != null) ? message.getTargetId() : "")
			.add(messageId, (message.getMessageId() != null) ? message.getMessageId() : "")
			.add(contextId, (message.getContextId() != null) ? message.getContextId() : "")
			.add(messageType, (message.getMessageType() != null) ? message.getMessageType().name() : "")
			.add(returnCode, (message.getReturnCode() != null) ? message.getReturnCode() : "")
			.add(payloadData, (message.getPayloadData() != null) ? message.getPayloadData() : "");
		return respBuilder.build().toString();
	}
	
	public static DataMessage createAckOKMessage(String sourceId, String targetId, String requestId) {
		DataMessage request = new DataMessage();
		request.setSourceId(sourceId);
		request.setTargetId(targetId);
		request.setMessageId(UUID.randomUUID().toString());
		request.setContextId(requestId);
		request.setMessageType(DataMessageType.ackOK);
		request.setReturnCode(OK);
		request.setPayloadData("");
		return request;
	}
	
	public static DataMessage createLocalServerInfoMessage(String sourceId, String targetId, String requestId, String hostname, String serverId, String sessionId, List<SessionInfo> activeSessions) {
		DataMessage request = new DataMessage();
		request.setSourceId(sourceId);
		request.setTargetId(targetId);
		request.setMessageId(UUID.randomUUID().toString());
		request.setContextId(requestId);
		request.setMessageType(DataMessageType.localServerInfoResponse);
		request.setReturnCode(OK);
		request.setPayloadData(getServerInfoJSON(hostname, serverId, sessionId, activeSessions));
		return request;
	}
	
	private static String getServerInfoJSON(String hostname, String serverId, String sessionId) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder
			.add("hostname", (hostname != null) ? hostname : "")
			.add("serverId", (serverId != null) ? serverId : "")
		    .add("sessionId", (sessionId != null) ? sessionId : "");
		return respBuilder.build().toString();
	}

	private static String getServerInfoJSON(String hostname, String serverId, String sessionId, List<SessionInfo> activeSessions) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder
			.add("hostname", (hostname != null) ? hostname : "")
			.add("serverId", (serverId != null) ? serverId : "")
		    .add("sessionId", (sessionId != null) ? sessionId : "");
		JsonArrayBuilder jsonArray = Json.createArrayBuilder();
		for (SessionInfo sessionInfo: activeSessions) {
			JsonObjectBuilder jsonSession = Json.createObjectBuilder();
			jsonSession
				.add("sessionId", (sessionInfo.getSessionId() != null) ? sessionInfo.getSessionId() : "")
				.add("started", (sessionInfo.getStarted() != null) ? sessionInfo.getStarted().getTime() : 0)
			    .add("serverId", (sessionInfo.getServerId() != null) ? sessionInfo.getServerId() : "");
			jsonArray.add(jsonSession);
		}
		respBuilder.add("sessionList", jsonArray);
		return respBuilder.build().toString();
	}

	public static String sessionInfoListToJson(List<SessionInfo> sessionInfos) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		JsonArrayBuilder jsonArray = Json.createArrayBuilder();
		for (SessionInfo sessionInfo: sessionInfos) {
			JsonObjectBuilder jsonSession = Json.createObjectBuilder();
			jsonSession
				.add("sessionId", (sessionInfo.getSessionId() != null) ? sessionInfo.getSessionId() : "")
				.add("started", (sessionInfo.getStarted() != null) ? sessionInfo.getStarted().getTime() : 0)
			    .add("serverId", (sessionInfo.getServerId() != null) ? sessionInfo.getServerId() : "");
			jsonArray.add(jsonSession);
		}
		respBuilder.add("sessionList", jsonArray);
		respBuilder.add("messageType", "activeSessionList");
		return respBuilder.build().toString();
	}

	public static String createSimpleMessagePayload(String message) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder
			.add("message", (message != null) ? message : "");
		return respBuilder.build().toString();
	}

	public static String createOKResponse(String message) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("returnCode", "OK");
		respBuilder.add("messageType", "replyMessage");
		respBuilder.add("message", message);
		return respBuilder.build().toString();
	}

	public static String createErrorResponse(String message) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("returnCode", "ERROR");
		respBuilder.add("messageType", "replyMessage");
		respBuilder.add("message", message);
		return respBuilder.build().toString();
	}

	public static String createServerInfoResponse(String hostname, String serverId) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("returnCode", "OK");
		respBuilder.add("messageType", "serverInfo");
		JsonObjectBuilder serverInfoObj = Json.createObjectBuilder();
		serverInfoObj.add("hostname", hostname);
		serverInfoObj.add("serverId", serverId);
		respBuilder.add("serverInfo", serverInfoObj);
		return respBuilder.build().toString();
	}
	
	public static String encodeSystemEvent(SystemEvent systemEvent) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("messageType", "systemEvent");
		respBuilder.add("systemEventType", systemEvent.getType().name());
		respBuilder.add("sessionId", systemEvent.getSessionId());
		return respBuilder.build().toString();
	}

	public static String createAdminResponse(String messageType, boolean loginSuccess) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("messageType", messageType);
		if (loginSuccess) {
			respBuilder.add("returnCode", "OK");
		} else {
			respBuilder.add("returnCode", "ERROR");
		}
		return respBuilder.build().toString();
	}

	public static String createAdminDataResponse(String userId, Date loginTime, String serverId, String hostName, boolean authorized) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("messageType", "adminData");
		if (authorized) {
			respBuilder.add("userId", userId);
			respBuilder.add("loginDate", loginTime.getTime());
		} else {
		}
		respBuilder.add("serverId", serverId);
		respBuilder.add("hostname", hostName);
		respBuilder.add("returnCode", "OK");
		respBuilder.add("authorized", authorized);
		return respBuilder.build().toString();
	}
	
	public static String createDisconnectWsSessionResponse(String wsSessionId, String status) {
		JsonObjectBuilder respBuilder = Json.createObjectBuilder();
		respBuilder.add("messageType", "disconnectWsSession");
		respBuilder.add("wsSessionId", wsSessionId);
		respBuilder.add("status", status);
		return respBuilder.build().toString();
	}

}
