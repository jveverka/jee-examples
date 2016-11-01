package ite.servlet.protocolupgrade.dto;

public class MessageData {
	
	private String message;
	private Long value;
	
	public MessageData() {
	}

	public MessageData(String message, Long value) {
		this.message = message;
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "MD:" + message + ":" + value;
	}
	
}
