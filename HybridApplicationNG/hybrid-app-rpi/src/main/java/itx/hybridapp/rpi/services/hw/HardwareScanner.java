package itx.hybridapp.rpi.services.hw;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HardwareScanner implements Runnable {
	
	private static final Logger logger = Logger.getLogger(HardwareScanner.class.getName());
	
	private RPiDevice device;
	private long sleepPeriod;
	private boolean shutdown;
	
	public HardwareScanner(RPiDevice device, long sleepPeriod) {
		this.device = device;
		this.sleepPeriod = sleepPeriod;
		this.shutdown = false;
	}

	@Override
	public void run() {
		logger.info("HW: started");
		while(!shutdown) {
			try {
				Thread.sleep(sleepPeriod);
				device.scanHardware();
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE,"HW: ERROR",e);
			}
		}
		logger.info("HW: stopped");
	}
	
	public void shutdown() {
		shutdown = true;
		logger.info("HW: shutdown");
	}

}
