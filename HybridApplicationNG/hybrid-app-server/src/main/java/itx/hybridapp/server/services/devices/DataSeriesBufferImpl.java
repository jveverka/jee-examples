package itx.hybridapp.server.services.devices;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeDataHolder;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.TimeSeriesDataResponse.Builder;


@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DataSeriesBufferImpl implements DataSeriesBuffer {
	
	private static final Logger logger = Logger.getLogger(DataSeriesBufferImpl.class.getName());
	private static final long historyInterval = 5*60*1000;
	private Map<String, Deque<TimeDataHolder>> buffers;
	
	@PostConstruct
	public void init() {
		logger.info("init ...");
		buffers = new ConcurrentHashMap<>();
	}

	@Override
	public void addData(String deviceId, TimeDataHolder timeData) {
		Deque<TimeDataHolder> buffer = buffers.get(deviceId);
		int truncatedCounter = 0;
		if (buffer == null) {
			buffer = new ConcurrentLinkedDeque<>();
			buffers.put(deviceId, buffer);
			buffer.addFirst(timeData);
		} else {
			buffer.addFirst(timeData);
			truncatedCounter = truncateBuffer(buffer);
		}
		logger.info("addData " + deviceId + ":" + buffer.size() + "/" + truncatedCounter);
	}

	@Override
	public TimeSeriesDataResponse getData(String deviceId) {
		logger.info("getData: " + deviceId);
		Deque<TimeDataHolder> buffer = buffers.get(deviceId);
		if (buffer != null) {
			Builder tsdrBuilder = TimeSeriesDataResponse.newBuilder()
					.setDeviceId(deviceId)
					.setHistoryInterval(historyInterval)
					.setTimeStamp(System.currentTimeMillis());
			buffer.forEach(t -> { tsdrBuilder.addTimeDataHolder(t); });
			return tsdrBuilder.build();
		} else {
			logger.severe("getData: ERROR");
		}
		return null;
	}
	
	private int truncateBuffer(Deque<TimeDataHolder> buffer) {
		int counter = 0;
		TimeDataHolder first = buffer.getFirst();
		TimeDataHolder last = buffer.getLast();
		if ((first.getTimeStamp() - last.getTimeStamp()) > historyInterval) {
			long treshold = first.getTimeStamp() - historyInterval;
			while (buffer.getLast().getTimeStamp() < treshold) {
				buffer.removeLast();
				counter++;
			}
		}
		return counter;
	}

}
