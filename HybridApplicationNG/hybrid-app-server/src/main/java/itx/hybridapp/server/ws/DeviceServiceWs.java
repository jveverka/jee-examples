package itx.hybridapp.server.ws;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListResponse;
import itx.hybridapp.server.services.devices.DeviceService;

@Path("/deviceservice")
public class DeviceServiceWs {

	private static final Logger logger = Logger.getLogger(DataServiceWs.class.getName());
	
	@Context
	private HttpServletRequest request; 

	@Inject
	private DeviceService deviceService;
	
	@POST
	@Path("/getDevices")
	@Produces({ProtoMediaType.APPLICATION_PROTOBUF, MediaType.APPLICATION_JSON})
	@Consumes({ProtoMediaType.APPLICATION_PROTOBUF, MediaType.APPLICATION_JSON})
	public Response getDevices() {
		logger.info("getDevices: " + request.getContentType() );
		DeviceListResponse deviceList = deviceService.getDevices();
		logger.info("getDevices done");
		return Response.ok(deviceList).type(request.getContentType()).build();
	}

}
