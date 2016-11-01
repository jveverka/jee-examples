package ite.examples.template.services.ws;

import ite.examples.template.services.SessionManagerService;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/webservice")
public class WebService {
	
	private static final Logger logger = Logger.getLogger(WebService.class.getName());

	@Inject
	private SessionManagerService sessionManager;
	
	@POST
	@Path("getdata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WSResponse getData(GetDataRequest request) {
		long sessionTimeout = sessionManager.getSessionTimeout(request.getSessionId());
		WSResponse response = new WSResponse("OK", "All Ok for session " + request.getSessionId() + " !", sessionTimeout);
		logger.info("WS: getData: " + request.getSessionId() + " sessionTimeout=" + sessionTimeout);
		return response;
	}

}
