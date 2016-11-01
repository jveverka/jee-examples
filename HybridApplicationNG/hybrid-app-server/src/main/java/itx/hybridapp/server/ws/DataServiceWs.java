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
import itx.hybridapp.common.protocols.DataServiceProtocol.TestRequest;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestResponse;
import itx.hybridapp.server.services.data.DataTestService;

@Path("/dataservice")
public class DataServiceWs {
	
	private static final Logger logger = Logger.getLogger(DataServiceWs.class.getName());
	
	@Context
	private HttpServletRequest request; 

	@Inject
	private DataTestService dataService;
	
	@POST
	@Path("/getData")
	@Produces({ProtoMediaType.APPLICATION_PROTOBUF, MediaType.APPLICATION_JSON})
	@Consumes({ProtoMediaType.APPLICATION_PROTOBUF, MediaType.APPLICATION_JSON})
	public Response getData(TestRequest testRequest) {
		logger.info("getData: " + testRequest.getData() + " " + request.getContentType() );
		String data = dataService.getData(testRequest.getData());
		TestResponse response = TestResponse.newBuilder().setData(data).build();
		return Response.ok(response).build();
	}

}
