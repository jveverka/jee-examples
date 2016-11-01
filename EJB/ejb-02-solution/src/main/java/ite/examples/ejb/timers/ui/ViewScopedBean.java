package ite.examples.ejb.timers.ui;

import ite.examples.ejb.timers.DelayedActionManager;
import ite.examples.ejb.timers.SingletonScopedBean;
import ite.examples.ejb.timers.StatelessBean;
import ite.examples.ejb.timers.TimerServiceManager;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.logging.Logger;

@ViewScoped
@Named("vscoped")
public class ViewScopedBean implements Serializable {

	private static final Logger logger = Logger.getLogger(ViewScopedBean.class.getName());
	
	@Inject
	private StatelessBean stateless;

	@Inject
	private SingletonScopedBean singleton;
	
	@Inject
	private TimerServiceManager timerManager;
	
	@Inject
	private DelayedActionManager delayedManager;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public int getSingletonCounter() {
		return singleton.getCounter();
	}

	public int getStatelessCounter() {
		return stateless.getCounter();
	}
	
	public void createTimerAction() {
		logger.info("createTimerAction ...");
		timerManager.createTimer();
	}

	public void cancelTimerAction() {
		logger.info("cancelTimerAction ...");
		timerManager.cancelTimer();
	}
	
	public void fireDelayedAction() {
		logger.info("fireDelayedAction ...");
		delayedManager.createDelayedAction();
	}
	
	public boolean isTimerCreated() {
		return timerManager.isTimerCreated();
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
