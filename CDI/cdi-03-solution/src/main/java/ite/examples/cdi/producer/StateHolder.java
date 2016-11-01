package ite.examples.cdi.producer;

import java.util.logging.Logger;


public class StateHolder {
	
	private static final Logger logger = Logger.getLogger(StateHolder.class.getName());

	private String state;
	
	public StateHolder() {
		logger.info("created ...");
	}
	
	public StateHolder(String state) {
		this.state = state;
		logger.info("created: state=" + state);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		logger.info("state=" + state);
	}

}
