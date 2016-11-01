package itx.hybridapp.server.services.dto;

import java.io.Serializable;

import com.google.protobuf.GeneratedMessageV3;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;

public class WsSessionMessageWrapper extends WsSessionMessage implements Serializable {
	
	private String protocol;
	private WrapperMessage message;
	
	public WsSessionMessageWrapper() {
		super();
	}

	public WsSessionMessageWrapper(String wsSessionId, WrapperMessage message, String protocol) {
		super(wsSessionId);
		this.message = message;
		this.protocol = protocol;
	}

	public WrapperMessage getMessage() {
		return message;
	}

	public void setMessage(WrapperMessage message) {
		this.message = message;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
