package itx.hybridapp.server.services.dto;

import java.io.Serializable;

public class WsSessionCreatedMessage extends WsSessionMessage implements Serializable {

	public WsSessionCreatedMessage() {
		super();
	}

	public WsSessionCreatedMessage(String wsSessionId) {
		super(wsSessionId);
	}
	
}
