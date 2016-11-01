package itx.hybridapp.javafx.messaging.events;

public class LoginEvent extends AppEvent {
	
	private String userName;
	private String httpSessionId;
	
	private LoginEvent(String userName, String httpSessionId) {
		this.userName = userName;
		this.httpSessionId = httpSessionId;
	}

	public String getUserName() {
		return userName;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private String userName;
		private String httpSessionId;
		
		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}
		
		public Builder setHttpSessionId(String httpSessionId) {
			this.httpSessionId = httpSessionId;
			return this;
		}
		
		public LoginEvent build() {
			return new LoginEvent(userName, httpSessionId);
		}
		
	}

}
