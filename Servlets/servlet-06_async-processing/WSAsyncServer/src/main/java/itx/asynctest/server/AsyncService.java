package itx.asynctest.server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("data")
public class AsyncService {
	
	private static final Logger logger = Logger.getLogger(AsyncService.class.getName());
	
	@Inject
	private DataService dataService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("async")
	public void getReportAsync(@Suspended final AsyncResponse asyncResponse, @QueryParam("delay") long delay) {
		logger.info("get async data ...");
		dataService.startBackgroundJobAsync(asyncResponse, delay, Thread.currentThread().getName());
		logger.info("get async done.");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("sync")
	public Response getReportSync(@QueryParam("delay") long delay) {
		logger.info("get sync data ...");
		try {
			Future<Report> upcommingReport = dataService.startBackgroundJobSync(delay, Thread.currentThread().getName());
			Report report = upcommingReport.get();
			logger.info("get sync done.");
			return Response.ok(report).build();
		} catch (ExecutionException | InterruptedException e) {
			logger.info("get sync failed.");
			return Response.serverError().build();
		}
	}

}
