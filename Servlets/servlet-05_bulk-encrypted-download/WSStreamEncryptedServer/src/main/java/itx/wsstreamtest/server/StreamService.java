package itx.wsstreamtest.server;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.stream.XMLStreamException;


@Path("data")
public class StreamService {
	
	private static final Logger logger = Logger.getLogger(StreamService.class.getName());
	
	@Inject
	private DataService dataService;
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("stream")
	public Response streamData(@QueryParam("password") String password, @QueryParam("size") int size, @QueryParam("delay") long delay) {
		logger.info("streaming data ...");
		
		try {

			StreamingOutput stream = new StreamingOutput() {
        	
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					try {
						long duration = System.currentTimeMillis();
						logger.info("streaming started: size=" + size + " password=" + password + " delay=" + delay);
						dataService.getEncryptedData(password, size, delay, output);
						duration = System.currentTimeMillis() - duration;  
						logger.info("streaming finished in " + duration + "ms");
					} catch (InterruptedException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | XMLStreamException e) {
						throw new WebApplicationException(e);
					}
				}
            
			};
			logger.info("streaming done.");
			return Response.ok(stream).build();
		
		} catch (Exception e) {
			logger.severe("streaming exception: " + e.getMessage());
	        return Response.serverError().build();
		}
	}
	
}
