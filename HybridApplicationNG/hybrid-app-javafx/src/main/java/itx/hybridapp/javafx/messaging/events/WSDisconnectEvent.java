package itx.hybridapp.javafx.messaging.events;


public class WSDisconnectEvent extends AppEvent {
	
	private WSDisconnectEvent() {
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		public WSDisconnectEvent build() {
			return new WSDisconnectEvent();
		}
		
	}

}
