package itx.hybridapp.javafx.messaging.events;

import java.util.List;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceInfo;

public class DeviceInfoListEvent extends AppEvent {
	
	private List<DeviceInfo> dil;
	
	private DeviceInfoListEvent(List<DeviceInfo> dil) {
		this.dil = dil;
	}
	
	public List<DeviceInfo> getDeviceInfoList() {
		return dil;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private List<DeviceInfo> dil;
		
		public Builder setDeviceInfoList(List<DeviceInfo> dil) {
			this.dil = dil;
			return this;
		}
		
		public DeviceInfoListEvent build() {
			return new DeviceInfoListEvent(dil);
		}
		
	}

}
