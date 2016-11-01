package itx.asynctest.server;

import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.container.AsyncResponse;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class DataService {
	
	private static final Logger logger = Logger.getLogger(DataService.class.getName());
	
    @Resource
    private ManagedExecutorService executorService;

    @Asynchronous
	public void startBackgroundJobAsync(AsyncResponse asyncResponse, long delay, String triggeredBy) {
		logger.info("startBackgroundJob ...");
		DataReportTask dataReportTask = new DataReportTask(asyncResponse, Thread.currentThread().getName(),triggeredBy,delay);
		executorService.submit(dataReportTask);
		logger.info("startBackgroundJob done.");
	}

	public Future<Report> startBackgroundJobSync(long delay, String triggeredBy) {
		logger.info("startBackgroundJob ...");
		DataReportTask dataReportTask = new DataReportTask(null, Thread.currentThread().getName(),triggeredBy,delay);
		Future<Report> upcommingReport = executorService.submit(dataReportTask);
		logger.info("startBackgroundJob done.");
		return upcommingReport;
	}

}
