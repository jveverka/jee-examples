package itx.hybridapp.javafx.websockets;

import java.io.IOException;
import java.util.logging.Logger;

import itx.hybridapp.common.client.websocket.WSClient;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;

public class WSService {
	
	private static final Logger logger = Logger.getLogger(WSService.class.getName());
	
	private static WSService SELF;
	
	private WSClient wsClient;
	private WSEventListenerImpl wsListener;
	
	private WSService(String wsUrl, String mediaType) {
		this.wsListener = new WSEventListenerImpl();
		this.wsClient = WSClient.buildClient(wsUrl, mediaType, wsListener);
	}
	
	public static void init(String wsUrl, String mediaType) {
		SELF = new WSService(wsUrl, mediaType);
	}
	
	public static WSService getInstance() {
		return SELF;
	}
	
	public void connect() {
		try {
			wsClient.startClient();
		} catch (Exception e) {
			logger.severe("WS connect Exception");
		}
	}
	
	public void sendMessage(WrapperMessage wm) {
		try {
			wsListener.sendMessage(wm);
		} catch (IOException e) {
			logger.severe("WS send message error");
		}
	}
	
	public void close() {
		wsClient.close();
	}

}
