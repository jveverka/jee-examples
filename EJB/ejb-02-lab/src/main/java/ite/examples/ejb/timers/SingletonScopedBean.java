package ite.examples.ejb.timers;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Named;

@Singleton
@Named("singleton")
public class SingletonScopedBean {
	
	private static final Logger logger = Logger.getLogger(SingletonScopedBean.class.getName());
	private int counter;
	
	public SingletonScopedBean() {
		logger.info("SINGLRTON SINGLETON SINGLETON");
	}
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		counter = 0;
	}
	
	@Schedule(second="*/20", hour="*", minute="*", persistent=false)
	public void scheduledAction() {
		logger.info("Singleton: scheduledAction [" + counter + "] ...");
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
