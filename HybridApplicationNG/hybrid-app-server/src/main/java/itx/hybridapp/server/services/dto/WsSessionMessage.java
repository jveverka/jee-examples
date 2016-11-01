package itx.hybridapp.server.services.dto;

import java.io.Serializable;

public class WsSessionMessage implements Serializable {
	
	private String wsSessionId;
	
	public WsSessionMessage() {
	}

	public WsSessionMessage(String wsSessionId) {
		this.wsSessionId = wsSessionId;
	}

	public String getWsSessionId() {
		return wsSessionId;
	}

	public void setWsSessionId(String wsSessionId) {
		this.wsSessionId = wsSessionId;
	}

}
