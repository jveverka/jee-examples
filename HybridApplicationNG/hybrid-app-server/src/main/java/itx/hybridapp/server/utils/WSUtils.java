package itx.hybridapp.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.Session;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

public final class WSUtils {
	
	public static void sendMessage(Session session, Message message, boolean useBinary) throws IOException {
		if (useBinary) {
			ByteArrayOutputStream entityOutputStream = new ByteArrayOutputStream();
			message.writeTo(entityOutputStream);
			ByteBuffer bb = ByteBuffer.wrap(entityOutputStream.toByteArray());
			session.getAsyncRemote().sendBinary(bb);
		} else {
			String jsonMessage = JsonFormat.printer().includingDefaultValueFields().print(message);
			session.getAsyncRemote().sendText(jsonMessage);
		}
	}

}
