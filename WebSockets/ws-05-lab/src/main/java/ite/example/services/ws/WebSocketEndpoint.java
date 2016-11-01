package ite.example.services.ws;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ite.example.services.EntityService;
import ite.example.services.SessionManager;
import ite.example.services.ws.dto.RequestMessage;
import ite.example.services.ws.dto.ResponseMessage;

@ServerEndpoint(value = "/data",
    decoders = { MessageDecoder.class },
    encoders = { MessageEncoder.class })
public class WebSocketEndpoint {
	
	private static final Logger logger = Logger.getLogger(WebSocketEndpoint.class.getName());
	
	@Inject
	private EntityService entityService;
	
	@Inject
	private SessionManager sessionManager;

	@OnMessage
    public ResponseMessage onMessage(RequestMessage message, Session session) {
		logger.info("Received: "+ message.toString() + " sessionId: " + session.getId());
		try {
			entityService.saveDataRecord(message.getTimestamp(), message.getMessage());
			return new ResponseMessage(System.currentTimeMillis(), message.getId(), message.getMessage(), 0, session.getId());
		} catch (Exception e) {
			return new ResponseMessage(System.currentTimeMillis(), message.getId(), message.getMessage(), 1, session.getId());
		}
    }
	
	@OnOpen
    public void onOpen(Session session) {
		logger.info("WebSocket opened: " + session.getId());
		sessionManager.addSession(session);
    }
	
	@OnClose
    public void onClose(Session session, CloseReason reason) {
		sessionManager.removeSession(session.getId());
		logger.info("Closing a WebSocket due to: " + reason.getCloseCode().getCode() + "/" + reason.getReasonPhrase() + " " + session.getId());
    }
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		sessionManager.removeSession(session.getId());
		logger.info("onError: " + session.getId());
	}
	
}
