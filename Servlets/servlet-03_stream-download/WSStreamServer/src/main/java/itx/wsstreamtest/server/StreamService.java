package itx.wsstreamtest.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;

@Path("data")
public class StreamService {
	
	private static final Logger logger = Logger.getLogger(StreamService.class.getName());
	
	@Inject
	private Configuration config;
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	//@Produces(MediaType.TEXT_PLAIN)
	@Path("stream")
	public Response streamData() {
		logger.info("streaming file " + config.getDataSourcePath());
		
		try {
    	File file = new File(config.getDataSourcePath());

    	StreamingOutput stream = new StreamingOutput() {
        	
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
            	long duration = System.currentTimeMillis();
            	logger.info("streaming started");
            	FileUtils.copyFile(file, output);
            	duration = System.currentTimeMillis() - duration;  
            	logger.info("streaming finished in " + duration + "ms");
            }
            
        };
        logger.info("done.");
        return Response.ok(stream).build();
		
		} catch (Exception e) {
			logger.severe("streaming exception: " + e.getMessage());
	        return Response.serverError().build();
		}
	}

}
