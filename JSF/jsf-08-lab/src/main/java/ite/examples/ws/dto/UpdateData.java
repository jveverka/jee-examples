package ite.examples.ws.dto;

public class UpdateData {
	
	private String returnCode;
	private int row;
	private int col;
	private String data;
	
	public UpdateData() {
	}
	
	public UpdateData(String returnCode, int row, int col, String data) {
		super();
		this.returnCode = returnCode;
		this.row = row;
		this.col = col;
		this.data = data;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
}
