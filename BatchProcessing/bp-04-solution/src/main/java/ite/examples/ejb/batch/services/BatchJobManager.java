package ite.examples.ejb.batch.services;

import ite.examples.ejb.batch.dto.BatchJobData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class BatchJobManager {
	
	private static final Logger logger = Logger.getLogger(BatchJobManager.class.getName());
	private Map<Long, BatchJobData> batchJobs;
	private static final long JOB_TTL = 1*60*1000;
	private JobOperator jobOperator;
	
	@Inject
	private UserMDBClientBean mdbClient;

	@PostConstruct
	private void init() { 
		logger.info("init ...");
		batchJobs = new HashMap<>(); 
		jobOperator = BatchRuntime.getJobOperator();
	}
	
	public void startJob() {
		jobOperator.start("create-report", new Properties());
	}
	
	public void createJobEntry(BatchJobData data) {
		logger.info("createJobEntry ...");
		synchronized(batchJobs) {
			batchJobs.put(data.getId(), data);
			mdbClient.sendTopicMessageAction("Job:" + data.getId() + ":created");
		}
	}

	public void updateProgress(Long jobId, Integer progress) {
		synchronized(batchJobs) {
			BatchJobData batchJob = batchJobs.get(jobId);
			batchJob.setProgress(progress);
			mdbClient.sendTopicMessageAction("Job:" + jobId + ":progress:" + progress);
		}
	}

	public void updateStatus(Long jobId, BatchResult status, Integer progress) {
		synchronized(batchJobs) {
			BatchJobData batchJob = batchJobs.get(jobId);
			batchJob.setResult(status);
			batchJob.setDuration(System.currentTimeMillis() - batchJob.getDuration().longValue());
			batchJob.setProgress(progress);
			mdbClient.sendTopicMessageAction("Job:" + jobId + ":status:" + status);
		}
	}

	public List<BatchJobData> getBatchJobs() {
		synchronized(batchJobs) {
			List<BatchJobData> jobs = new ArrayList<>(batchJobs.values());
			return jobs;
		}
	}

	@Schedule(second="*/10", minute="*", hour="*", persistent=false)
	public void cleanBuffer() {
		logger.info("clean batch job buffer ...");
		boolean removed = false;
		synchronized(batchJobs) {
			List<Long> toRemove = new ArrayList<>();
			for (Long jobId: batchJobs.keySet()) {
				BatchJobData batchJob = batchJobs.get(jobId);
				if (!BatchResult.RUNNING.equals(batchJob.getResult())) {
					if (batchJob.getStarted().getTime() < (System.currentTimeMillis() - JOB_TTL)) {
						toRemove.add(jobId);
						removed = true;
					}
				}
			}
			if (removed) {
				for (Long jobId: toRemove) {
					batchJobs.remove(jobId);
				}
				mdbClient.sendTopicMessageAction("Jobs:removed");
			}
		}
	}
		
	@PreDestroy
	private void deinit() { 
		logger.info("deinit ...");
	}

}
