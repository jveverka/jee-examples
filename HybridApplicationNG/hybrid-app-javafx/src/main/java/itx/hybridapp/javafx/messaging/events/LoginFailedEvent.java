package itx.hybridapp.javafx.messaging.events;

public class LoginFailedEvent extends AppEvent {
	
	private String message;

	private LoginFailedEvent(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		private String message;
		
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public LoginFailedEvent build() {
			return new LoginFailedEvent(message);
		}
		
	}
	
}
