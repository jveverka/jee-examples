package ite.examples.services;

import java.util.ArrayList;
import java.util.List;

public class SimpleRowHolder {
	
	private String[] data;
	
	public SimpleRowHolder(String[] data) {
		this.data = new String[data.length];
		for (int i=0; i<data.length; i++) {
			this.data[i] = data[i];
		}
	}
	
	public void updateValue(int index, String value) {
		this.data[index] = value;
	}
	
	public int getSize() {
		return data.length;
	}

	public SimpleRowHolder getCopy() {
		return new SimpleRowHolder(this.data);
	}
	
	public String getCell(int index) {
		return data[index];
	}
	
	public List<String> getValues() {
		List<String> result = new ArrayList<>(data.length);
		for (String value: data) {
			result.add(value);
		}
		return result;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}
	
	public List<String> toList() {
		List<String> result = new ArrayList<>();
		for (String cell: data) {
			result.add(cell);
		}
		return result;
	}
}
