package ite.examples.servlet.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="TestServlet", urlPatterns={"/sessiontest"})
public class TestServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(TestServlet.class.getName());
	private static final int MAX_INACTIVE_INTERVAL = 20;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("processing GET request ...");
		processRequest(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("processing POST request ...");
		processRequest(req, resp);
	}
	
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		try {
			String outMessage = "";
			if (session.isNew()) {
				outMessage = "NEW registered session id=" + session.getId();
				session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
				session.setAttribute("sessionAttribute", new SessionAttribute());
			} else {
				outMessage = "Already registered session id=" + session.getId();
			}
			logger.info("session Id=" + session.getId());
			logger.info("session timeout= " + session.getMaxInactiveInterval());
			logger.info("session lastAccess= " + session.getLastAccessedTime());
			PrintWriter pw = resp.getWriter();
			pw.write("<!DOCTYPE html>");
			pw.write("<html><head></head><body><h1>HTTP Session Test</h1>" + outMessage + "</body></html>");
			pw.close();
		} catch (IOException e) {
			logger.severe("processRequest IOException: " + e.getMessage());
		}
	}

}
