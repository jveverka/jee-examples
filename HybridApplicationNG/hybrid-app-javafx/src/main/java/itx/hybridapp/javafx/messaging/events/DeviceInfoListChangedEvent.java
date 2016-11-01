package itx.hybridapp.javafx.messaging.events;

public class DeviceInfoListChangedEvent extends AppEvent {

	private DeviceInfoListChangedEvent() {
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		
		public DeviceInfoListChangedEvent build() {
			return new DeviceInfoListChangedEvent();
		}
		
	}
}
