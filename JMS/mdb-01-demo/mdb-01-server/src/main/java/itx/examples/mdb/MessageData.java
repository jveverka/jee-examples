package itx.examples.mdb;

import java.io.Serializable;
import java.util.Date;

public class MessageData implements Serializable {
	
	private Date date;
	private String tansportType;
	private String message;
	
	public MessageData() {
	}
	
	public MessageData(Date date, String tansportType, String message) {
		this.date = date;
		this.tansportType = tansportType;
		this.message = message;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTansportType() {
		return tansportType;
	}

	public void setTansportType(String tansportType) {
		this.tansportType = tansportType;
	}
	
}
