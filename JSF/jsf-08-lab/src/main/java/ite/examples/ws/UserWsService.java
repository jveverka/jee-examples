package ite.examples.ws;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ite.examples.services.access.UserAccessService;
import ite.examples.ws.dto.AuthResponse;
import ite.examples.ws.dto.LogOnRequest;

@Path("/userws")
public class UserWsService {
	
	@Inject
	private UserAccessService userService;

	@Context 
	private HttpServletRequest servletRequest;
	
	@POST
	@Path("getSessionId")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public AuthResponse getSessionId() {
		return new AuthResponse(servletRequest.getSession().getId());
	}
	
	@POST
	@Path("logOn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public AuthResponse logon(LogOnRequest request) {
		boolean result = userService.doLoginAction(request.getUserName(), request.getPassword());
		if (!result) {
			return new AuthResponse("ERROR");
		}
		return new AuthResponse("OK");
	}

	@POST
	@Path("logOff")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public AuthResponse logoff() {
		userService.doLogoutAction();
		return new AuthResponse("OK");
	}

}
