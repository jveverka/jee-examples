package itx.hybridapp.server.ws;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.protocols.UserAccessProtocol;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginResponse;
import itx.hybridapp.server.services.useraccess.UserAccessService;

@Path("/useraccess")
public class UserAccessWs {
	
	private static final Logger logger = Logger.getLogger(UserAccessWs.class.getName());
	
	@Context
	private HttpServletRequest request; 
	
	@Inject
	private UserAccessService uaService;
	
	@POST
	@Path("/login")
	@Produces({ProtoMediaType.APPLICATION_PROTOBUF, MediaType.APPLICATION_JSON})
	@Consumes({ProtoMediaType.APPLICATION_PROTOBUF, MediaType.APPLICATION_JSON})
	public Response login(UserAccessProtocol.LoginRequest loginRequest) {
		logger.info("login: " + request.getContentType());
		String sessionId = request.getSession().getId();
		try {
			String protocol = ProtoMediaType.getProtocolOrDefault(request.getContentType());
			List<String> roles = uaService.loginHttpSession(sessionId, protocol, loginRequest.getUserName(), loginRequest.getPassword());
			LoginResponse.Builder builder = LoginResponse.newBuilder();
			roles.forEach(s -> builder.addRole(s));
			builder.setUserName(loginRequest.getUserName());
			logger.info("login OK");
			return Response.ok(builder.build()).build();
		} catch (LoginException e) {
			logger.info("login FAILED");
			return Response.status(Status.FORBIDDEN).build();
		}
	}
	
	@POST
	@Path("/isValidSession")
	public Response isValidSession() {
		logger.info("isValidSession: " + request.getContentType());
		String sessionId = request.getSession().getId();
		if (uaService.isValidHttpSession(sessionId)) {
			return Response.ok().build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@POST
	@Path("/logout")
	public Response logout() {
		logger.info("logout: " + request.getContentType());
		String sessionId = request.getSession().getId();
		uaService.logoutHttpSession(sessionId);
		request.getSession().invalidate();
		return Response.ok().build();
	}

}
