package ite.examples.ws.dto;

public class UpdateRequest {
	
	private int row;
	private int col;
	private String data;
	
	public UpdateRequest() {
	}

	public UpdateRequest(int row, int col, String data) {
		this.row = row;
		this.col = col;
		this.data = data;
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
