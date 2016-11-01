package ite.servlet.protocolupgrade.dto;

public class MessageDataBuilder {
	
	private String message;
	private Long value;
	
	private MessageDataBuilder() {
	}
	
	public MessageDataBuilder setMessage(String message) {
		this.message = message;
		return this;
	}

	public MessageDataBuilder setValue(Long value) {
		this.value = value;
		return this;
	}
	
	public MessageData build() {
		return new MessageData(message, value);
	}

	public static MessageDataBuilder builder() {
		return new MessageDataBuilder();
	}

}
