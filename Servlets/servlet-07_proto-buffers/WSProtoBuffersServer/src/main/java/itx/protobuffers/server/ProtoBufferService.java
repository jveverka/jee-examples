package itx.protobuffers.server;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import itx.protobuffers.common.UsersProtoc.User;


@Path("data")
public class ProtoBufferService {
	
	private static final Logger logger = Logger.getLogger(ProtoBufferService.class.getName());
	
	@Inject
	private DataService dataService;
	
	@GET
	@Produces("application/protobuf")
	@Path("getAllUsers")
	public Response getAllUsers() {
		logger.info("getAllUsers ...");
		return Response.ok(dataService.getAllUsers()).build();
	}

	@GET
	@Produces("application/protobuf")
	@Path("getUserByName")
	public Response getUserByName(@QueryParam("userName") String userName) {
		logger.info("get sync data ...");
		User user = dataService.getUserByName(userName);
		if (user != null) {
			return Response.ok(user).build();
		}
		return Response.serverError().build();
	}

}
