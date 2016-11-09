package itx.hybridapp.javafx.messaging.events;

public class LoginEvent extends AppEvent {
	
	private String userName;
	private String httpSessionId;
	private String mediaType;
	
	private LoginEvent(String userName, String httpSessionId, String mediaType) {
		this.userName = userName;
		this.httpSessionId = httpSessionId;
		this.mediaType = mediaType;
	}

	public String getUserName() {
		return userName;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}
	
	public String getMediatype() {
		return mediaType;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private String userName;
		private String httpSessionId;
		private String mediaType;
		
		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}
		
		public Builder setHttpSessionId(String httpSessionId) {
			this.httpSessionId = httpSessionId;
			return this;
		}
		
		public Builder setMediaType(String mediaType) {
			this.mediaType = mediaType;
			return this;
		}
		
		public LoginEvent build() {
			return new LoginEvent(userName, httpSessionId, mediaType);
		}
		
	}

}
