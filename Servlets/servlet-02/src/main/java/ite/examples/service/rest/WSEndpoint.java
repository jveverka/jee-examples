package ite.examples.service.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/wsservice")
public class WSEndpoint {
	
	private final static Logger logger = Logger.getLogger(WSEndpoint.class.getName());

	@GET
	@Path("get")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getMessage(@QueryParam("message")String message) {
		logger.info("getMessage: " + message);
        return "response to GET request: " + message;
    }
	
	@POST
	@Path("post")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String postMessage(String message) {
		logger.info("postMessage: " + message);
        return "response to POST request: " + message;
    }
	
}
