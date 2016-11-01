package ite.examples.jpa.crud.dto;

import java.io.Serializable;

public class MessageTO implements Serializable {
	
	private String message;
	
	public MessageTO() {
	}
	
	public MessageTO(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
