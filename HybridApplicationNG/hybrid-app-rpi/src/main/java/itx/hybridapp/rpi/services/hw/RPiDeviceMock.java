package itx.hybridapp.rpi.services.hw;

import java.util.Random;
import java.util.logging.Logger;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.ControlOutputEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;

public class RPiDeviceMock implements RPiDevice {
	
	private static final Logger logger = Logger.getLogger(RPiDeviceMock.class.getName());
	
	private boolean[] controlOutputs;
	private ControlOutputEventListener controlListener;
	private float temperature;
	private float humidity;
	private float pressure;
	private Random random;
	private HardwareScanner hwScanner;
	
	public RPiDeviceMock() {
		logger.info("initializing RaspberryPi MOCK device ...");
		controlOutputs = new boolean[4];
		temperature = 20.3f;
		humidity = 43f;
		pressure = 965.13f;
		random = new Random();
		hwScanner = new HardwareScanner(this, 5000);
		new Thread(hwScanner).start();
	}

	@Override
	public GetStatusResponse getStatus(String deviceId, String replyToWsSessionId) {
		logger.info("getStatus");
		GetStatusResponse response = 
				GetStatusResponse.newBuilder()
				.setDeviceId(deviceId)
				.setReplyToWsSessionId(replyToWsSessionId)
				.setTemperature(temperature)
				.setPressure(pressure)
				.setRelativeHumidity(humidity)
				.addControlOutputs(controlOutputs[0])
				.addControlOutputs(controlOutputs[1])
				.addControlOutputs(controlOutputs[2])
				.addControlOutputs(controlOutputs[3])
				.build();
		return response;
	}

	@Override
	public void setControlOutput(SetControlOutputRequest value) {
		logger.info("setControlOutput: " + value.getPinId() + ":" + value.getState());
		controlOutputs[value.getPinId()] = value.getState();
		if (controlListener != null) {
			ControlOutputEvent event = 
					ControlOutputEvent.newBuilder()
					.setPinId(value.getPinId())
					.setState(value.getState())
					.build();
			controlListener.onControlStatusChange(event);
		}
	}

	@Override
	public void setControlOutputEventListener(ControlOutputEventListener listener) {
		controlListener = listener;
	}

	@Override
	public void setButtonEventListener(ButtonEventListener listener) {
	}
	
	private float getRandomValue(float input) {
		int rnd = random.nextInt(200);
		rnd = rnd - 100;
		return input = input + (input/10)*(rnd/100f); 
	}

	@Override
	public SensorEvent getSensorData() {
		logger.info("getSensorData");
		SensorEvent sensors = SensorEvent.newBuilder()
				.setTemperature(temperature)
				.setRelativeHumidity(humidity)
				.setPressure(pressure)
				.build();
		return sensors;
	}

	@Override
	public void shutdown() {
		logger.info("shutdown");
		hwScanner.shutdown();
	}

	@Override
	public void scanHardware() {
		temperature = getRandomValue(temperature);
		humidity = getRandomValue(humidity);
		pressure = getRandomValue(pressure);
		logger.info("scanHardware: " + temperature + ":" + humidity + ":" + pressure);
	}

}
