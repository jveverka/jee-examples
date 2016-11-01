package ite.examples.ejb.batch.services;

import ite.examples.ejb.batch.dto.BatchJobData;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BatchJobManagerProxy {
	
	@Inject
	private BatchJobManager jobManager;

	public void startJob() {
		jobManager.startJob();
	}
	
	public List<BatchJobData> getBatchJobs() {
		return jobManager.getBatchJobs();
	}
	
}
