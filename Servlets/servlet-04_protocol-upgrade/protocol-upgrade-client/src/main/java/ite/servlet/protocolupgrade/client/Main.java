package ite.servlet.protocolupgrade.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import ite.servlet.protocolupgrade.dto.MessageData;
import ite.servlet.protocolupgrade.dto.MessageUtils;

public class Main {
	
	private final static Logger logger = Logger.getLogger(Main.class.getName());
	
	private static final int PORT = 8080;
	private static final String HOSTNAME = "localhost";
	private static final String URL = "/protocol-upgrade-server/customProtocolEndpoint";
	
	public static void main(String[] args) {
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			logger.info("Upgrade protocol client started: ");
			socket = new Socket(InetAddress.getByName(HOSTNAME), PORT);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			PrintWriter pw = new PrintWriter(os);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			DataOutputStream dos = new DataOutputStream(os);
			DataInputStream dis = new DataInputStream(is);
			
			logger.info("sending upgrade request ...");
			MessageUtils.writeUpgradeRequest(pw, HOSTNAME, URL);
			logger.info("reading upgrade response ...");
			boolean upgradeAccepted = MessageUtils.readUpgradeResponse(br);
			if (upgradeAccepted) {
				logger.info("Protocol upgrade OK, sending data to server ...");
				MessageData request = new MessageData("rq1",0L);
				logger.info("REQUEST: " + request.toString());
				MessageUtils.writeMessageData(dos, new MessageData("req",0L));
				MessageData response = MessageUtils.readMessageData(dis);
				logger.info("RESPONSE: " + response.toString());
				logger.info("FINISHED.");
			} else {
				logger.info("Protocol upgrade FAILED.");
			}
			logger.info("Upgrade protocol client finished.");
		} catch (UnknownHostException e) {
			logger.info("UnknownHostException: " + e.getMessage());
		} catch (IOException e) {
			logger.info("IOException");
		} finally {
			MessageUtils.closeAllQuietly(os, is, socket);
		}
		
	}

}
