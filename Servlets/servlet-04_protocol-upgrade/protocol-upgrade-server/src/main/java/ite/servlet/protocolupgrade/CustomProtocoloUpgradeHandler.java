package ite.servlet.protocolupgrade;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;

import ite.servlet.protocolupgrade.dto.MessageData;
import ite.servlet.protocolupgrade.dto.MessageDataBuilder;
import ite.servlet.protocolupgrade.dto.MessageUtils;

public class CustomProtocoloUpgradeHandler implements HttpUpgradeHandler {
	
	private final static Logger logger = Logger.getLogger(CustomProtocoloUpgradeHandler.class.getName());

	@Override
	public void init(WebConnection wc) {
		ServletInputStream is = null;
		ServletOutputStream os = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			logger.info("init ...");
			is = wc.getInputStream();
			os = wc.getOutputStream();
			dos = new DataOutputStream(os);
			dis = new DataInputStream(is);
			logger.info("processing ...");
			MessageData request = MessageUtils.readMessageData(dis);
			logger.info("REQUEST: " + request.toString());
			MessageData response = MessageDataBuilder.builder()
					.setMessage("resp+" + request.getMessage())
					.setValue(request.getValue() + 1)
					.build();
			logger.info("RESPONSE: " + response.toString());
			MessageUtils.writeMessageData(dos, response);
			logger.info("done.");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException: ", e);
		} finally {
			MessageUtils.closeAllQuietly(dos, dis);
		}
	}

	@Override
	public void destroy() {
		logger.info("destroy ...");
	}

}
