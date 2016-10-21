package ite.clusterdemo.services.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="clusterapp_datalog")
public class DataLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Date timeStamp;
	private String sourceId;
	private String targetId;
	private String contextId;
	private DataMessageType messageType;
	private String returnCode;
	private String payloadData;
	
	public DataLog() {
	}

	public DataLog(DataMessage message) {
		this.timeStamp = null;
		this.sourceId = message.getSourceId();
		this.targetId = message.getTargetId();
		this.contextId = message.getContextId();
		this.messageType = message.getMessageType();
		this.returnCode = message.getReturnCode();
		this.payloadData = message.getPayloadData();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
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
	
}
