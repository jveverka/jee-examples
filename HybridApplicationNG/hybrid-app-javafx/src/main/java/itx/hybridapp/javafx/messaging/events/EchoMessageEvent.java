package itx.hybridapp.javafx.messaging.events;

public class EchoMessageEvent extends AppEvent {

	private EchoMessageEvent() {
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		public EchoMessageEvent build() {
			return new EchoMessageEvent();
		}
		
	}
}
