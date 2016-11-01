package ite.examples.ejb.timers;

import java.io.Serializable;

public class TimerData implements Serializable {
	
	private String name;

	public TimerData() {
	}
	
	public TimerData(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
