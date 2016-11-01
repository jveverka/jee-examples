package ite.servlet.protocolupgrade;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ite.servlet.protocolupgrade.dto.MessageUtils;

@WebServlet(urlPatterns = { "/customProtocolEndpoint" })
public class ProtocolUpgradeServlet extends HttpServlet {

	private final static Logger logger = Logger.getLogger(ProtocolUpgradeServlet.class.getName());

	@Override
	public void init() throws ServletException {
		logger.info("init ...");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		logger.info("ProtocolUpgradeServlet:");
		try {
			String protocolName = request.getHeader("Upgrade");
			if (MessageUtils.PROTOCOL_NAME.equals(protocolName)) {
				/* Accept upgrade request */
				response.setStatus(101);
				response.setHeader("Upgrade", MessageUtils.PROTOCOL_NAME);
				response.setHeader("Connection", "Upgrade");
				/* Delegate the connection to the upgrade handler */
				request.upgrade(CustomProtocoloUpgradeHandler.class);
				logger.info("ProtocolUpgradeServlet: upgrade OK");
				/* (the service method returns immediately) */
			} else {
				logger.severe("Unsupported upgrade protocol: " + protocolName);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported upgrade protocol: " + protocolName);
				response.flushBuffer();
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException: ", e);
		} catch (ServletException e) {
			logger.log(Level.SEVERE, "ServletException: ", e);
		}
	}
	
	@Override
	public void destroy() {
		logger.info("destroy ...");
	}

}
