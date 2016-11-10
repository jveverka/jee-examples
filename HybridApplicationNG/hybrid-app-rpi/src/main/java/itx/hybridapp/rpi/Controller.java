package itx.hybridapp.rpi;

import java.util.logging.Logger;

import com.google.inject.Inject;

import itx.hybridapp.common.client.websocket.WSClient;
import itx.hybridapp.common.client.websocket.WSEventListener;
import itx.hybridapp.rpi.services.config.ConfigService;

public class Controller {
	
	private static final Logger logger = Logger.getLogger(Controller.class.getName());
	private static final int WAIT_PERIOD = 3000;
	
	@Inject
	private ConfigService config;
	
	public void mainLoop() {
		boolean running = true;
		logger.info("mainLoop start: " + config.getWsBaseUrl());
		logger.info("HOSTNAME: " + config.getHostName());
		WSEventListener wsListener = new WSEventListenerImpl(config); 
		WSClient wsClient = WSClient.buildClient(config.getWsUrl(), config.getMediaType(), wsListener);
		while (running) {
			try {
				logger.info("ws client start");
				wsClient.startClientBlocking();
				Thread.sleep(WAIT_PERIOD);
				logger.info("stopped, reconecting ...");
			} catch (Exception e) {
				try {
					logger.info("waiting ...");
					Thread.sleep(WAIT_PERIOD);
				} catch (InterruptedException exc) {
					logger.severe("InterruptedException");
				}
			}
		}
		wsClient.shutdown();
	}

}
