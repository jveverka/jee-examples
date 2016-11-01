package itx.asynctest.server;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.ws.rs.container.AsyncResponse;

public class DataReportTask implements Callable<Report> {
	
	private static final Logger logger = Logger.getLogger(DataReportTask.class.getName());
	
	private AsyncResponse asyncResponse; 
	private String startedBy;
	private String triggeredBy;
	private long delay;
	
	public DataReportTask(AsyncResponse asyncResponse, String startedBy, String triggeredBy, long delay) {
		this.asyncResponse = asyncResponse;
		this.startedBy = startedBy;
		this.triggeredBy = triggeredBy;
		this.delay = delay;
	}

	@Override
	public Report call() throws Exception {
		logger.info("Data Report task started ...");
		long started = System.currentTimeMillis();
		Thread.sleep(delay);
		long duration = System.currentTimeMillis() - started;
		Report report = new Report(started, duration, Thread.currentThread().getName(), "OK", startedBy, triggeredBy);
		logger.info("Data Report task done: " + report);
		if (asyncResponse != null) asyncResponse.resume(report);
		return report;
	}

}
