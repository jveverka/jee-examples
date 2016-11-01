package itx.wsstreamtest.client;

public class Configuration {

	private final int size = 100000000;  //data items in xml
	private final int delay = 0; //nano seconds
	private final String password = "secret"; //encryption password
	private final static String SERVICE_URL = "http://localhost:8080/wsstreambulk/ws/data/stream";
	private final static Configuration self = new Configuration();
	
	public static Configuration getInstance() {
		return self;
	}
	
	public String getServiceURL() {
		return SERVICE_URL + "?size=" + size + "&password=" + password + "&delay=" + delay;
	}
	
	public String getPassword() {
		return password;
	}
	
}
