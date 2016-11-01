package itx.hybridapp.rpi.services.hw;

import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.ButtonEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.ControlOutputEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.GetStatusResponse;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SensorEvent;
import itx.hybridapp.common.protocols.DeviceServiceProtocol.SetControlOutputRequest;
import itx.rpi.hardware.gpio.sensors.BMP180;
import itx.rpi.hardware.gpio.sensors.HTU21DF;

public class RPiDeviceReal implements RPiDevice {
	
	private static final Logger logger = Logger.getLogger(RPiDeviceReal.class.getName());
	
	private static final int maxPorts = 4;

	private ControlOutputEventListener controlListener;
	private ButtonEventListener buttonListener;
	private float temperature1;
	private float temperature2;
	private float pressure;
	private float humidity;
	private GpioPinDigitalOutput[] ports;
	private boolean[] portValues;
	private GpioPinListenerDigitalImpl digitalPinlistener;

	private BMP180 bmp180;
	private HTU21DF htu21d;

	private HardwareScanner hwScanner;

	public RPiDeviceReal() {
		logger.info("HWC: initializing RaspberryPi device ...");
		GpioController gpio = GpioFactory.getInstance();
		ports = new GpioPinDigitalOutput[maxPorts];
		ports[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
		ports[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
		ports[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
		ports[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
		portValues = new boolean[maxPorts];
		bmp180 = new BMP180();
		htu21d = new HTU21DF();
		try {
			getHwState();
		} catch (Exception e) {
			logger.severe("HWC: read Exception: " + e.getMessage());
		}
		digitalPinlistener = new GpioPinListenerDigitalImpl(this);
		GpioPinDigitalInput pushButton = null;
		pushButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
		pushButton.addListener(digitalPinlistener);
		pushButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
		pushButton.addListener(digitalPinlistener);
		pushButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
		pushButton.addListener(digitalPinlistener);
		pushButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
		pushButton.addListener(digitalPinlistener);

		hwScanner = new HardwareScanner(this, 5000);
		new Thread(hwScanner).start();

		logger.info("HWC: initialization done.");
	}

	@Override
	public GetStatusResponse getStatus(String deviceId, String replyToWsSessionId) {
		logger.info("getStatus");
		GetStatusResponse response = 
				GetStatusResponse.newBuilder()
				.setDeviceId(deviceId)
				.setReplyToWsSessionId(replyToWsSessionId)
				.setTemperature(temperature1)
				.setPressure(pressure)
				.setRelativeHumidity(humidity)
				.addControlOutputs(portValues[0])
				.addControlOutputs(portValues[1])
				.addControlOutputs(portValues[2])
				.addControlOutputs(portValues[3])
				.build();
		return response;
	}

	@Override
	public synchronized void setControlOutput(SetControlOutputRequest value) {
		logger.info("setControlOutput: " + value.getPinId() + ":" + value.getState());
		ports[value.getPinId()].setState(value.getState());
		portValues[value.getPinId()] = ports[value.getPinId()].getState().isHigh();
		ControlOutputEvent event = ControlOutputEvent.newBuilder()
				.setPinId(value.getPinId())
				.setState(portValues[value.getPinId()])
				.build();
		controlListener.onControlStatusChange(event);
	}

	@Override
	public void setControlOutputEventListener(ControlOutputEventListener listener) {
		controlListener = listener;
	}

	@Override
	public void setButtonEventListener(ButtonEventListener listener) {
		buttonListener = listener;
	}
	
	public void sendButtonStateEvent(int buttonId, boolean state) {
		ButtonEvent buttonEvent = ButtonEvent.newBuilder()
				.setButtonId(buttonId)
				.setState(state)
				.build();
		buttonListener.onButtonEvent(buttonEvent);
	}

	public synchronized void togglePortStateChage(int portId) {
		boolean state = !ports[portId].getState().isHigh();
		ports[portId].setState(state);
		portValues[portId] = ports[portId].getState().isHigh();
		ControlOutputEvent event = ControlOutputEvent.newBuilder()
				.setPinId(portId)
				.setState(portValues[portId])
				.build();
		controlListener.onControlStatusChange(event);
	}
	
	private synchronized void getHwState() throws Exception {
		logger.info("getHwState");
		long duration = System.nanoTime();
		temperature1 = bmp180.readTemperature();
		pressure = (bmp180.readPressure()/100F);
		temperature2 = htu21d.readTemperature();
		humidity = htu21d.readHumidity();
		for (int i = 0; i < ports.length; i++) {
			PinState pinState = ports[i].getState();
			if (pinState != null) {
				portValues[i] = pinState.isHigh();
			} else {
				portValues[i] = false;
			}
		}
		duration = System.nanoTime() - duration;
		logger.info("data scan took: " + (duration/1000000) + "ms " + temperature1 + ":" + pressure + ":" + humidity);
	}

	@Override
	public synchronized SensorEvent getSensorData() {
		logger.info("getStatus");
		SensorEvent sensors = SensorEvent.newBuilder()
				.setTemperature(temperature1)
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
		try {
			getHwState();
		} catch (Exception e) {
			logger.severe("scanHardware Exception");
		}
	}

}
