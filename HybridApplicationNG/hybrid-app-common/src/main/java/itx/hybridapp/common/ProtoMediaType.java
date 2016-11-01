package itx.hybridapp.common;

import javax.ws.rs.core.MediaType;

public final class ProtoMediaType {
	
	public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	public static final String APPLICATION_PROTOBUF = "application/protobuf";
	
	public static boolean isBinaryProtocol(String mediaType) {
		return APPLICATION_PROTOBUF.equals(mediaType);
	}

	public static String getProtocol(boolean isBinaryProtocol) {
		if (isBinaryProtocol) {
			return APPLICATION_PROTOBUF;
		}
		return APPLICATION_JSON;
	}
	
	public static String getProtocolOrDefault(String mediaType) {
		if (APPLICATION_PROTOBUF.equals(mediaType)) {
			return APPLICATION_PROTOBUF;
		}
		return APPLICATION_JSON;
	}
	
	public static String getDefaultProtocol() {
		return APPLICATION_JSON;
	}

}
