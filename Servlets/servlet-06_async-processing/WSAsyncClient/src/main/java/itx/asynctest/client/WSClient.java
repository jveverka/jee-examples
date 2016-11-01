package itx.asynctest.client;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class WSClient {
	
	private static final Logger logger = Logger.getLogger(WSClient.class.getName());
	
	private Configuration config;
	
	public WSClient(Configuration config) {
		this.config = config;
	}
	
	public void getData() {
		try {
			logger.info("downloading data from: " + config.getServiceURL() );
			long duration = System.currentTimeMillis();
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(config.getServiceURL());
			Builder builder = target.request();
			Response response = builder.get();
			String responseStr = response.readEntity(String.class);
			logger.info("response: " + responseStr);
			duration = System.currentTimeMillis() - duration;
			logger.info("finished in: " + duration + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
