package itx.hybridapp.javafx.messaging.events;

public class WSConnectEvent extends AppEvent {
	
	private String wsSessionId;
	
	private WSConnectEvent(String wsSessionId) {
		this.wsSessionId = wsSessionId;
	}
	
	public String getWsSessionId() {
		return wsSessionId;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private String wsSessionId;
		
		public Builder setWsSessionId(String wsSessionId) {
			this.wsSessionId = wsSessionId;
			return this;
		}
		
		public WSConnectEvent build() {
			return new WSConnectEvent(wsSessionId);
		}
		
	}

}
