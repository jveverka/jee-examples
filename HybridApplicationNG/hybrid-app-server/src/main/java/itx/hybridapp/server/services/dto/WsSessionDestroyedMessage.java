package itx.hybridapp.server.services.dto;

import java.io.Serializable;

public class WsSessionDestroyedMessage extends WsSessionMessage implements Serializable {
	
	public WsSessionDestroyedMessage() {
		super();
	}

	public WsSessionDestroyedMessage(String wsSessionId) {
		super(wsSessionId);
	}

}
