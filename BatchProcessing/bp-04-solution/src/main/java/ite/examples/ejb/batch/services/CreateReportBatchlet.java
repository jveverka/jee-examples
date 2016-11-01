package ite.examples.ejb.batch.services;

import ite.examples.ejb.batch.dto.BatchJobData;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.Batchlet;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Dependent
@Named("CreateReportBatchlet")
public class CreateReportBatchlet implements Batchlet {
	
	private static final Logger logger = Logger.getLogger(CreateReportBatchlet.class.getName());
	private static final int MAX_LOOPS = 100;
	private static final int DELAY = 500;

	@Inject
	private JobContext jobContext;
	
	@Inject
	private BatchJobManager batchJobManager;
	
	@Override
	public String process() throws Exception {
		BatchResult result = BatchResult.RUNNING;
		logger.info("JOB: [" + jobContext.getExecutionId() + "] just started ...");
		Long jobId = new Long(jobContext.getExecutionId());
		BatchJobData jobData = new BatchJobData(jobId, "Report Job", result, new Date(), 0L);
		batchJobManager.createJobEntry(jobData);
		try {
			//JobOperator jobOperator = BatchRuntime.getJobOperator();
			//Properties jobParameters = jobOperator.getParameters(jobContext.getExecutionId());
			for (int i=0; i<MAX_LOOPS; i++) {
				logger.info("JOB: [" + jobContext.getExecutionId() + "," + i + "] running ...");
				batchJobManager.updateProgress(jobId, i);
				Thread.sleep(DELAY);
			}
			result = BatchResult.SUCCESS;
			batchJobManager.updateStatus(jobId, result, MAX_LOOPS);
			logger.info("JOB: [" + jobContext.getExecutionId() + "] done.");
		} catch (Exception e) {
			result = BatchResult.FAILED;
			batchJobManager.updateStatus(jobId, result, MAX_LOOPS);
		}
		return result.name();
	}

	@Override
	public void stop() throws Exception {
	}

}
