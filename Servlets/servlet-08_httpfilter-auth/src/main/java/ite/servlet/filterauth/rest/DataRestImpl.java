package ite.servlet.filterauth.rest;

import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import ite.servlet.filterauth.dto.DataResponse;
import ite.servlet.filterauth.service.DataService;

@Path("/datarest")
@PermitAll
public class DataRestImpl {
	
	private static final Logger logger = Logger.getLogger(DataRestImpl.class.getName());
	
	@Inject
	private DataService dataService;
	
	@Context
	private SecurityContext securityContext;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getData")
	@RolesAllowed({"demo", "user", "admin", "root"})
	public Response getData(@QueryParam(value = "request") String request) {
		logger.info("principal name: " + securityContext.getUserPrincipal().getName());
		logger.info("user in role demo: " + securityContext.isUserInRole("demo"));
		logger.info("user in role user: " + securityContext.isUserInRole("user"));
		logger.info("user in role admin: " + securityContext.isUserInRole("admin"));
		logger.info("user in role root: " + securityContext.isUserInRole("root"));
		DataResponse dr = new DataResponse(dataService.getData(request));
		return Response.ok(dr).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/setData")
	@RolesAllowed({"admin", "root"})
	public Response setData(@QueryParam(value = "response") String response) {
		logger.info("principal name: " + securityContext.getUserPrincipal().getName());
		logger.info("user in role demo: " + securityContext.isUserInRole("demo"));
		logger.info("user in role user: " + securityContext.isUserInRole("user"));
		logger.info("user in role admin: " + securityContext.isUserInRole("admin"));
		logger.info("user in role root: " + securityContext.isUserInRole("root"));
		dataService.setData(response);
		DataResponse dr = new DataResponse(response);
		return Response.ok(dr).build();
	}

}
