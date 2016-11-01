package itx.hybridapp.javafx.devicesform;

import javafx.beans.property.SimpleStringProperty;

public class DeviceTableInfo {

	private SimpleStringProperty deviceId;
    private SimpleStringProperty wsSessionId;
    private SimpleStringProperty connected;
    
    public DeviceTableInfo() {
    }

	public DeviceTableInfo(String deviceId, String wsSessionId,	String connected) {
		super();
		this.deviceId = new SimpleStringProperty(deviceId);
		this.wsSessionId = new SimpleStringProperty(wsSessionId);
		this.connected = new SimpleStringProperty(connected);
	}

	public String getDeviceId() {
		return deviceId.get();
	}

	public void setDeviceId(String deviceId) {
		this.deviceId.set(deviceId);
	}

	public String getWsSessionId() {
		return wsSessionId.get();
	}

	public void setWsSessionId(String wsSessionId) {
		this.wsSessionId.set(wsSessionId);
	}

	public String getConnected() {
		return connected.get();
	}

	public void setConnected(String connected) {
		this.connected.set(connected);
	}
    
}
