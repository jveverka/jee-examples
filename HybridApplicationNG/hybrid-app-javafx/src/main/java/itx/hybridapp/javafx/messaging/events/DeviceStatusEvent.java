package itx.hybridapp.javafx.messaging.events;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;

public class DeviceStatusEvent extends AppEvent {
	
	private GetStatusResponse data;
	
	private DeviceStatusEvent(GetStatusResponse data) {
		this.data = data;
	}
	
	public GetStatusResponse getData() {
		return data;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		private GetStatusResponse data;
		
		public Builder setData(GetStatusResponse data) {
			this.data = data;
			return this;
		}
		
		public DeviceStatusEvent build() {
			return new DeviceStatusEvent(data);
		}
		
	}

}
