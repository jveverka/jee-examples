package itx.hybridapp.javafx.services;

import itx.hybridapp.common.ProtoMediaType;

public class ConfigService {
	
	private static final String VERSION = "1.0";
	private static ConfigService SELF;

	private String baseUrl;
	private String wsBaseUrl;

	private ConfigService() {
		String address = System.getProperty("serverAddress");
		if (address == null) {
			address = "localhost:8080";
		}
		baseUrl = "http://" + address + "/hybridapp/ws";
		wsBaseUrl = "ws://" + address + "/hybridapp";
	}
	
	public static ConfigService getInstance() {
		if (SELF == null) {
			SELF = new ConfigService();
		}
		return SELF;
	}
	
	public String getAPIVersion() {
		return VERSION;
	}
	
	public String getBaseURL() {
		return baseUrl;
	}
	
	public String getWsBaseUrl() {
		return wsBaseUrl;
	}
	
	public String getMediaType() {
		return ProtoMediaType.APPLICATION_PROTOBUF;
	}
	
	public String getWsUrl() {
		return wsBaseUrl + "/ws/wsendpoint";
	}

}
