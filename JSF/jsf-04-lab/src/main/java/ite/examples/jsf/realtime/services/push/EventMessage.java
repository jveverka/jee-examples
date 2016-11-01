package ite.examples.jsf.realtime.services.push;

import ite.examples.jsf.realtime.services.DataSet;

/**
 * DTO used to transport event data event consumers.
 * @author jveverka
 *
 */
public class EventMessage {

	private String eventType;
	private DataSet dataSet;
	
	public EventMessage() {
	}
	
	/**
	 * Create an instance of EventMessage 
	 * @param type - type of the message
	 * @param description - description, may be text or JSON formated data for consumption in JavaScript runtime
	 */
	public EventMessage(String eventType, DataSet dataSet) {
		super();
		this.eventType = eventType;
		this.dataSet = dataSet;
	}

	public String getEventType() {
		return eventType;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
}
