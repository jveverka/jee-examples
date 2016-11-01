package itx.wsstreamtest.client;

public class Configuration {

	private final static String SERVICE_URL = "http://192.168.30.154:8080/wsstream/ws/data/stream";
	private final static String TARGET_DIRECTORY = "/opt/binaryblob.out";
	private final static Configuration self = new Configuration();
	
	public static Configuration getInstance() {
		return self;
	}
	
	public String getServiceURL() {
		return SERVICE_URL;
	}
	
	public String getTargetDirectory() {
		return TARGET_DIRECTORY;
	}
	
}
