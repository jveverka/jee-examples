package ite.examples.ejb.timers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import java.util.logging.Logger;

@Stateless
public class StatelessBean {

	private static final Logger logger = Logger.getLogger(StatelessBean.class.getName());
	private int counter;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		counter = 0;
	}
	
	@Schedule(second="*/15", hour="*", minute="*", persistent=false)
	public void scheduledAction() {
		logger.info("Stateless: scheduledAction [" + counter + "] ...");
		counter++;
	}
	
	public int getCounter() {
		return counter;
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
