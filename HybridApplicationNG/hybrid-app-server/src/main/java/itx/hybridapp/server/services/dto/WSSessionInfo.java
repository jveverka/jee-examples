package itx.hybridapp.server.services.dto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import com.google.protobuf.Message;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.server.utils.WSUtils;

public class WSSessionInfo {
	
	private Session session;
	private String userName;
	private String protocol;
	private List<String> roles;
	private boolean isBinaryProtocol;
	
	public WSSessionInfo(String userName, Session session, String protocol, List<String> roles) {
		this.userName = userName;
		this.session = session;
		this.protocol = protocol;
		this.roles = roles;
		this.isBinaryProtocol = ProtoMediaType.isBinaryProtocol(protocol);
	}

	public String getUserName() {
		return userName;
	}

	public Session getSession() {
		return session;
	}

	public String getProtocol() {
		return protocol;
	}
	
	public String getId() {
		return session.getId();
	}
	
	public void sendBinary(ByteBuffer byteBuffer) {
		session.getAsyncRemote().sendBinary(byteBuffer);
	}
	
	public void sendText(String text) {
		session.getAsyncRemote().sendText(text);
	}
	
	public void sendMessage(Message message) {
		try {
			WSUtils.sendMessage(session, message, isBinaryProtocol);
		} catch (IOException e) {
		}
	}
	
	public boolean usesBinaryProtocol() {
		return isBinaryProtocol;
	}
	
	public void close() {
		try {
			session.close();
		} catch (IOException e) {
		}
	}

	public List<String> getRoles() {
		return new ArrayList<>(roles);
	}

}
