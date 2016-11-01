package itx.hybridapp.rpi.services.hw;

import java.util.logging.Logger;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GpioPinListenerDigitalImpl implements GpioPinListenerDigital {
	
	private static final Logger logger = Logger.getLogger(GpioPinListenerDigitalImpl.class.getName());
	
	private RPiDeviceReal rpiDevice;
	
	public GpioPinListenerDigitalImpl(RPiDeviceReal rpiDevice) {
		this.rpiDevice = rpiDevice;
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		String buttonId = event.getPin().getName();
		boolean state = event.getState().isHigh();
		logger.info("handleGpioPinDigitalStateChangeEvent: " + buttonId + ":" + state);
		switch (buttonId) {
		case "GPIO 4":
			rpiDevice.sendButtonStateEvent(0, state);
			if (state) { rpiDevice.togglePortStateChage(0); }
			return;
		case "GPIO 5":
			rpiDevice.sendButtonStateEvent(1, state);
			if (state) { rpiDevice.togglePortStateChage(1); }
			return;
		case "GPIO 6":
			rpiDevice.sendButtonStateEvent(2, state);
			if (state) { rpiDevice.togglePortStateChage(2); }
			return;
		case "GPIO 7":
			rpiDevice.sendButtonStateEvent(3, state);
			if (state) { rpiDevice.togglePortStateChage(3); }
			return;
		}
	}

}
