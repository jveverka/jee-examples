package ite.clusterdemo.services.dto;

import java.io.Serializable;

public class DataMessage implements Serializable {
	
	private String sourceId;   //source of the message (originator) 
	private String targetId;   //target of the message (recepient)
	private String messageId;  //unique id of the message (in scope of whole application, not only in client scope)
	private String contextId;  //context of the message (if this is reply, id of request message)
	private DataMessageType messageType;//message type
	private String returnCode; //return code OK, ERROR, ...
	private String payloadData;//payload data of the message (JSON string, XML, ...)
	
	public DataMessage() {
	}

	public DataMessage(String sourceId, String targetId, String messageId, String contextId, DataMessageType messageType,
			String returnCode, String payloadData) {
		super();
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.messageId = messageId;
		this.contextId = contextId;
		this.messageType = messageType;
		this.returnCode = returnCode;
		this.payloadData = payloadData;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public DataMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(DataMessageType messageType) {
		this.messageType = messageType;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getPayloadData() {
		return payloadData;
	}

	public void setPayloadData(String payloadData) {
		this.payloadData = payloadData;
	}

	@Override
	public String toString() {
		return "DataMessage [sourceId=" + sourceId + ", targetId=" + targetId + ", messageId=" + messageId
				+ ", contextId=" + contextId + ", messageType=" + messageType.name() + ", returnCode=" + returnCode
				+ ", payloadData=" + payloadData + "]";
	}

}
