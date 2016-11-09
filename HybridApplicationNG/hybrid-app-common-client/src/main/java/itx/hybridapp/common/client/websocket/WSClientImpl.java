package itx.hybridapp.common.client.websocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.protocols.CommonAccessProtocol.WrapperMessage;
import itx.hybridapp.common.protocols.UserAccessProtocol.LoginRequest;

@ClientEndpoint
public class WSClientImpl implements WSClient, WSMessagePublisher {
	
	private final static Logger logger = Logger.getLogger(WSClientImpl.class.getName());
	
	private CountDownLatch latch;
	private Session session;
	private String socketUrl;
	private WSEventListener eventListener;
	private boolean isBinaryProtocol;
	
	protected WSClientImpl(String socketUrl, String mediaType, WSEventListener eventListener) {
		this.socketUrl = socketUrl;
		this.eventListener = eventListener;
		this.isBinaryProtocol = ProtoMediaType.isBinaryProtocol(mediaType);
		this.latch = null;
	}
	
	@OnOpen
    public void onOpen(Session session) {
		logger.info("onOpen");
		this.session = session;
		eventListener.onSessionCreated(session.getId());
    }
 
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
		logger.fine("onMessage: ");
		WrapperMessage.Builder builder = WrapperMessage.newBuilder();
		JsonFormat.parser().merge(message, builder);
		WrapperMessage wm = builder.build();
		eventListener.onMessage(wm);
    }

    @OnMessage
    public void onMessage(byte[] message, Session session) throws IOException {
		logger.fine("onMessage: ");
		WrapperMessage wm = WrapperMessage.parseFrom(message);
		eventListener.onMessage(wm);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s close because of %s", session.getId(), closeReason));
        session = null;
        eventListener.onSessionClosed();
        unblock();
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info(String.format("Session %s close because of %s", session.getId(), throwable));
        session = null;
        eventListener.onSessionError();
        unblock();
    }

	@Override
    public void startClient() throws Exception {
    	logger.info("startClient started: " + socketUrl);
    	this.eventListener.onInit(this);
    	ClientManager client = ClientManager.createClient();
    	try {
            client.connectToServer(this, new URI(socketUrl));
        } catch (DeploymentException | URISyntaxException e) {
            throw new Exception(e);
        }    	
    }

	@Override
    public void startClientBlocking() throws Exception {
    	logger.info("startClientBlocking started: " + socketUrl);
    	this.latch = new CountDownLatch(1);
    	this.eventListener.onInit(this);
    	ClientManager client = ClientManager.createClient();
    	try {
            client.connectToServer(this, new URI(socketUrl));
            latch.await();
        } catch (DeploymentException | URISyntaxException | InterruptedException e) {
            throw new Exception(e);
        }    	
    }

	@Override
	public void sendMessage(Message mesage) throws IOException {
		if (session == null) return;
		logger.info("sendMessage: useBinary=" + isBinaryProtocol);
		if (isBinaryProtocol) {
			ByteArrayOutputStream entityOutputStream = new ByteArrayOutputStream();
			mesage.writeTo(entityOutputStream);
			ByteBuffer bb = ByteBuffer.wrap(entityOutputStream.toByteArray());
			session.getAsyncRemote().sendBinary(bb);
		} else {
			String jsonMessage = JsonFormat.printer().includingDefaultValueFields().print(mesage);
			session.getAsyncRemote().sendText(jsonMessage);
		}
	}

    public void unblock() {
    	if (latch != null) {
    		latch.countDown();
    	}
    }

	@Override
	public void login(String protocol, String userName, String password) throws IOException {
		LoginRequest loginRequest = LoginRequest.newBuilder()
				.setUserName(userName)
				.setPassword(password)
				.setProtocol(protocol)
				.build();
		WrapperMessage wm = WrapperMessage.newBuilder().setLoginRequest(loginRequest).build();
		sendMessage(wm);
	}

	@Override
	public void login(String httpSessionId, String protocol, String userName, String password) throws IOException {
		LoginRequest loginRequest = LoginRequest.newBuilder()
				.setUserName(userName)
				.setPassword(password)
				.setProtocol(protocol)
				.setHttpSessionId(httpSessionId)
				.build();
		WrapperMessage wm = WrapperMessage.newBuilder().setLoginRequest(loginRequest).build();
		sendMessage(wm);
	}

	@Override
	public void close() {
		if (session != null) {
			try {
				session.close();
			} catch (IOException e) {
				logger.severe("session close exception");
			}
		}
		unblock();
	}

	@Override
	public void shutdown() {
		eventListener.onShutdown();
	}
	
}
