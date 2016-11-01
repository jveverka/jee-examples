package itx.wsstreamtest.client;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class WSClient {
	
	private static final Logger logger = Logger.getLogger(WSClient.class.getName());
	
	private Configuration config;
	
	public WSClient(Configuration config) {
		this.config = config;
	}
	
	public void getData() {
		try {
			File file = new File(config.getTargetDirectory());
			if (file.exists() && file.isFile()) {
				file.delete();
			}
			logger.info("downloading data from: " + config.getServiceURL() );
			long duration = System.currentTimeMillis();
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(config.getServiceURL());
			
			//InputStream stream = target.request(MediaType.TEXT_PLAIN).get(InputStream.class);
			InputStream stream = target.request(MediaType.APPLICATION_OCTET_STREAM).get(InputStream.class);
			
			Path path = Paths.get(config.getTargetDirectory());
			
			logger.info("reading stream ...");
			Files.copy(stream, path);
			duration = System.currentTimeMillis() - duration;
			logger.info("finished in: " + duration + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
