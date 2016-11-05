package itx.examples.swagger.ws;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import itx.examples.swagger.ws.dto.ApplicationInfo;
import itx.examples.swagger.ws.dto.DataResponse;

@Stateless
@Path("/swaggers")
@Api(value="/swaggers")
public class SwaggerEndpoint {
	
	private static final Logger logger = Logger.getLogger(SwaggerEndpoint.class.getName());
	
	@GET
	@Path("/info")
	@ApiOperation(value="getApplicationInfo", notes="Get application info data.")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApplicationInfo() {
		logger.info("getApplicationInfo");
		ApplicationInfo ai = new ApplicationInfo("wildfly-swagger-demo", "1.0", System.currentTimeMillis());
		return Response.ok(ai).build();
	}
	
	@GET
	@Path("/data")
	@ApiOperation(value="geData", notes="Get data.")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response geData(@QueryParam("data") String data) {
		if (data == null) {
			DataResponse dr = new DataResponse("XXXX");
			return Response.ok(dr).build();
		}
		logger.info("geData: " + data);
		DataResponse dr = new DataResponse(data.toUpperCase());
		return Response.ok(dr).build();
	}

}
