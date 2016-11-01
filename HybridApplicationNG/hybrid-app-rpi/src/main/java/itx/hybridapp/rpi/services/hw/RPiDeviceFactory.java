package itx.hybridapp.rpi.services.hw;

import java.util.logging.Logger;

public class RPiDeviceFactory {
	
	private static final Logger logger = Logger.getLogger(RPiDeviceFactory.class.getName());
	
	private static RPiDevice rpiDevice;
	
	public static RPiDevice getInstance() {
		if (rpiDevice == null) {
			String osArch = System.getProperty("os.arch");
			if ("arm".equals(osArch)) {
				logger.info("RPiDeviceFactory: using real rpi driver ...");
				rpiDevice = new RPiDeviceReal();
			} else {
				logger.info("RPiDeviceFactory: using mock rpi driver ...");
				rpiDevice = new RPiDeviceMock();
			}
		}
		return rpiDevice;
	}

}
