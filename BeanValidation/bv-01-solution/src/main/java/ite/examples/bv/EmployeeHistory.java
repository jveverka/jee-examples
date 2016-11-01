package ite.examples.bv;

import java.util.Date;

@ChronologicalDates
public class EmployeeHistory {
	
	private Date hired;
	private Date fired;
	
	public EmployeeHistory() {
	}
	
	public EmployeeHistory(Date hired, Date fired) {
		this.hired = hired;
		this.fired = fired;
	}
	
	public Date getHired() {
		return hired;
	}
	
	public void setHired(Date hired) {
		this.hired = hired;
	}
	
	public Date getFired() {
		return fired;
	}
	
	public void setFired(Date fired) {
		this.fired = fired;
	}
	
	@Override
	public String toString() {
		return "EH:" + hired + "::" + fired;
	}

}
