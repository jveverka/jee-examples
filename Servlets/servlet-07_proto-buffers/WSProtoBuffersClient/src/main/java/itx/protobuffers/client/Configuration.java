package itx.protobuffers.client;

public class Configuration {

	private final String userName = "john"; 
	private final static String SERVICE_URL = "http://localhost:8080/wsprotobuffers/ws/data";
	private final static Configuration self = new Configuration();
	
	public static Configuration getInstance() {
		return self;
	}

	public String getServiceURLUserList() {
		return SERVICE_URL + "/getAllUsers";
	}

	public String getServiceURLUser() {
		return SERVICE_URL + "/getUserByName?userName=" + userName;
	}
	
}
