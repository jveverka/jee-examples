package itx.hybridapp.javafx.messaging.events;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataResponse;

public class TimeSeriesDataEvent extends AppEvent {
	
	private TimeSeriesDataResponse data;
	
	private TimeSeriesDataEvent(TimeSeriesDataResponse data) {
		this.data = data;
	}
	
	public TimeSeriesDataResponse getData() {
		return data;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private TimeSeriesDataResponse data;
		
		public Builder setData(TimeSeriesDataResponse data) {
			this.data = data;
			return this;
		}
		
		public TimeSeriesDataEvent build() {
			return new TimeSeriesDataEvent(data);
		}
		
	}
}
