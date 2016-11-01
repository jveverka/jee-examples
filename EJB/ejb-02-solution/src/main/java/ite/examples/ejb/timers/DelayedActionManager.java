package ite.examples.ejb.timers;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerService;

@Stateless
public class DelayedActionManager {

	private static final Logger logger = Logger.getLogger(DelayedActionManager.class.getName());
	private static final int TIMEOUT = 5000;
	
	@Resource
	private TimerService timerService;
	
	public void createDelayedAction() {
		logger.info("Creating delayed timer, delay=" + TIMEOUT + "ms");
		timerService.createTimer(TIMEOUT, null);
	}
	
	@Timeout
	public void delayedAction() {
		logger.info("Delayed Timer Action !");
	}
	
}
