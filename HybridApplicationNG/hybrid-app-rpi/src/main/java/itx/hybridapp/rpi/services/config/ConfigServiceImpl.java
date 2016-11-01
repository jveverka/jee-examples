package itx.hybridapp.rpi.services.config;

import java.net.UnknownHostException;

import itx.hybridapp.common.ProtoMediaType;

public class ConfigServiceImpl implements ConfigService {
	
	private String wsBaseUrl;
	private String deviceId;
	private boolean isRealDevice;
	
	public ConfigServiceImpl() {
		String address = System.getProperty("serverAddress");
		if (address == null) {
			address = "localhost:8080";
		}
		wsBaseUrl = "ws://" + address + "/hybridapp";
		deviceId = System.getProperty("deviceId");
		if (deviceId == null) {
			deviceId = "device:" + System.currentTimeMillis();
			isRealDevice = false;
		} else {
			isRealDevice = true;
		}
	}

	@Override
	public String getWsBaseUrl() {
		return wsBaseUrl;
	}

	@Override
	public String getMediaType() {
		return ProtoMediaType.APPLICATION_PROTOBUF;
	}

	@Override
	public String getUserName() {
		if (isRealDevice) return "pi";
		return "pisim";
	}

	@Override
	public String getPassword() {
		if (isRealDevice) return "pi123";
		return "pisim123";
	}

	@Override
	public String getWsUrl() {
		return wsBaseUrl + "/ws/wsendpoint";
	}

	@Override
	public String getHostName() {
		try {
			return java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

}
