package ite.servlet.filterauth.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ite.servlet.filterauth.dto.LoginResponse;
import ite.servlet.filterauth.service.AuthService;
import ite.servlet.filterauth.service.LoginException;

@Path("/authrest")
public class AuthRestImpl {
	
	private static final Logger logger = Logger.getLogger(AuthRestImpl.class.getName());
	
	@Context
	private HttpServletRequest httpRequest;
	
	@Inject
	private AuthService authService;

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam(value = "userName") String userName, @QueryParam(value = "password") String password) {
		logger.info("login: " + userName);
		try {
			List<String> roles = authService.login(userName, password, httpRequest.getSession().getId());
			return Response.ok(new LoginResponse(roles)).build();
		} catch (LoginException e) {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@GET
	@Path("/isValidUser")
	public Response isValidUser() {
		logger.info("isValidUser");
		if(authService.isValidSession(httpRequest.getSession().getId())) {
			return Response.ok().build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@GET
	@Path("/logout")
	public Response logout() {
		logger.info("logout");
		authService.logout(httpRequest.getSession().getId());
		httpRequest.getSession().invalidate();
		return Response.ok().build();
	}

}
