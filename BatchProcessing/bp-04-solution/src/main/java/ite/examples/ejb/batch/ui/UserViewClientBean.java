package ite.examples.ejb.batch.ui;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import ite.examples.ejb.batch.dto.BatchJobData;
import ite.examples.ejb.batch.services.BatchJobManagerProxy;
import ite.examples.ejb.batch.ui.push.EventDispatcher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("userview")
public class UserViewClientBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserViewClientBean.class.getName());
	private List<BatchJobData> batchJobs;
	
	@Inject
	private BatchJobManagerProxy batchJobManager;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public List<BatchJobData> getBatchJobs() {
		return batchJobs;
	}
	
	public void startNewJobAction() {
		batchJobManager.startJob();
	}
	
	public void reloadDataAction() {
		batchJobs = batchJobManager.getBatchJobs();
	}
	
	public String getPushChannelName() {
		return EventDispatcher.PFPUSH_USER_CHANNEL;
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
