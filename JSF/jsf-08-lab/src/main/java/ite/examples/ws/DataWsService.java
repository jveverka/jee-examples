package ite.examples.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ite.examples.services.SimpleRowHolder;
import ite.examples.services.data.UserSessionService;
import ite.examples.ws.dto.InitialData;
import ite.examples.ws.dto.UpdateData;
import ite.examples.ws.dto.UpdateRequest;

@Path("/dataws")
public class DataWsService {
	
	@Inject
	private UserSessionService userService;

	@POST
	@Path("getInitalData")
	@Produces(MediaType.APPLICATION_JSON)
	public InitialData getInitalData() {
		InitialData response = new InitialData();
		List<List<String>> data = new ArrayList<>();
		for (SimpleRowHolder row: userService.getData()) {
			data.add(row.getValues());
		}
		response.setData(data);
		response.setReturnCode("OK");
		return response;
	}

	@POST
	@Path("updateData")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateData updateData(UpdateRequest updateRequest) {
		InitialData response = new InitialData();
		boolean result = userService.updateData(updateRequest.getRow(), updateRequest.getCol(), updateRequest.getData());
		return new UpdateData("OK", updateRequest.getRow(), updateRequest.getCol(), updateRequest.getData());
	}

}
