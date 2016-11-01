package ite.servlet.protocolupgrade.dto;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public final class MessageUtils {
	
	private final static Logger logger = Logger.getLogger(MessageUtils.class.getName());
	
	public static final String PROTOCOL_NAME = "CUSTOMP";

	public static void closeAllQuietly(AutoCloseable... acs) {
		for (AutoCloseable ac : acs) {
			closeQuietly(ac);
		}
	}

	public static void closeQuietly(AutoCloseable ac) {
		if (ac != null) {
			try {
				ac.close();
			} catch (Exception e) {
				logger.severe("Exception: " + e.getMessage());
			}
		}
	}

	public static void writeUpgradeRequest(PrintWriter pw, String hostName, String url) throws IOException {
		pw.println("GET " + url + " HTTP/1.1");
		pw.println("Host: " + hostName);
		pw.println("Connection: Upgrade");
		pw.println("Upgrade: " + PROTOCOL_NAME);
		pw.println("");
		pw.flush();
	}
	
	public static boolean readUpgradeResponse(BufferedReader br) throws IOException {
		boolean upgradeAccepted = false;
		String line;
		while((line = br.readLine()) != null) {
			if ("HTTP/1.1 101 Switching Protocols".equals(line)) {
				upgradeAccepted = true;
			}
			if (line.startsWith("Date:")) {
				break;
			}
		}
		return upgradeAccepted;
	}

	public static void writeMessageData(DataOutputStream dos, MessageData messageData) throws IOException {
		dos.writeLong(messageData.getValue().longValue());
		dos.writeUTF(messageData.getMessage());
		dos.flush();
	}

	public static MessageData readMessageData(DataInputStream dis) throws IOException {
		MessageData messageData = MessageDataBuilder.builder()
				.setValue(new Long(dis.readLong()))
				.setMessage(dis.readUTF())
				.build();
		return messageData;
	}

}
