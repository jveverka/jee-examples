package itx.hybridapp.javafx.messaging.events;

public class LogoutEvent extends AppEvent {

	private LogoutEvent() {
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		public LogoutEvent build() {
			return new LogoutEvent();
		}
		
	}
	
}
