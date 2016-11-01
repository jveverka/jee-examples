package itx.asynctest.client;

public class Configuration {

	private final int delay = 5000; //milliseconds
	private final static String SERVICE_URL = "http://localhost:8080/wsasync/ws/data/async";
	private final static Configuration self = new Configuration();
	
	public static Configuration getInstance() {
		return self;
	}
	
	public String getServiceURL() {
		return SERVICE_URL + "?delay=" + delay;
	}
	
}
