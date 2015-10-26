package ite.examples.ws.dto;

import java.util.List;

public class InitialData {
	
	private String returnCode;
	private List<List<String>> data;
	
	public InitialData() {
	}
	
	public String getReturnCode() {
		return returnCode;
	}
	
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	public List<List<String>> getData() {
		return data;
	}
	
	public void setData(List<List<String>> data) {
		this.data = data;
	}

}
