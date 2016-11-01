package itx.hybridapp.common.client.websocket;

import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;

/**
 * web socket client event listener
 * @author gergej
 *
 */
public interface WSEventListener {
	
	public void onInit(WSMessagePublisher messagePublisher);
	
	public void onSessionCreated(String wsSessionId);
	
	public void onMessage(WrapperMessage wm);
	
	public void onSessionClosed();
	
	public void onSessionError();
	
	public void onShutdown();

}
