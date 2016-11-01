package ite.examples.ejb.timers;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Singleton
public class TimerServiceManager {
	
	private static final Logger logger = Logger.getLogger(TimerServiceManager.class.getName());
	private String timerName;
	private Timer myTimer;
	private int counter;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		timerName = "myTimer";
		myTimer = null;
		counter = 0;
	}
	
	@Resource
	private TimerService timerService;

	public void createTimer() {
		if (myTimer != null) {
			logger.severe("TimerServiceManager: Timer " + timerName + " is already created !");
		}
		logger.info("TimerServiceManager: Creating " + timerName + " timer ...");
		ScheduleExpression se = new ScheduleExpression().second("*/30").minute("*").hour("*");
		myTimer = timerService.createCalendarTimer(se, new TimerConfig(new TimerData(timerName), false));
	}
	
	public boolean isTimerCreated() {
		return myTimer != null;
	}

	public void cancelTimer() {
		if (myTimer == null) {
			logger.severe("TimerServiceManager: No such timer " + timerName + " !");
		}
		logger.info("TimerServiceManager: Canceling " + timerName + " timer ...");
		myTimer.cancel();
		myTimer = null;
		counter = 0;
	}

	@Timeout
	public void doTimerAction(Timer timer) {
		TimerData td = (TimerData)timer.getInfo();
		logger.info("fired timer: [" + counter + "] " + td.getName());
		counter++;
	}

}
