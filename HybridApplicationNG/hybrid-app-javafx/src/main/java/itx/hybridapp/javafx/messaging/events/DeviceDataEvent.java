package itx.hybridapp.javafx.messaging.events;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceEvent;

public class DeviceDataEvent extends AppEvent {
	
	private DeviceEvent data;

	private DeviceDataEvent(DeviceEvent data) {
		this.data = data;
	}
	
	public DeviceEvent getData() {
		return data;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		private DeviceEvent data;
		
		public Builder setData(DeviceEvent data) {
			this.data = data;
			return this;
		}
		
		public DeviceDataEvent build() {
			return new DeviceDataEvent(data);
		}
		
	}
	
}
