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
	
	@Inject
	private SessionManager sessionManager;
	
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
		boolean isValidSession = sessionManager.checkSession(session);
		try {
			String outMessage = "";
			if (isValidSession) {
				outMessage = "Already registered session id=" + session.getId();
			} else {
				outMessage = "NEW registered session id=" + session.getId();
			}
			PrintWriter pw = resp.getWriter();
			pw.write("<!DOCTYPE html>");
			pw.write("<html><head></head><body><h1>HTTP Session Test</h1>" + outMessage + "</body></html>");
			pw.close();
		} catch (IOException e) {
			logger.severe("processRequest IOException: " + e.getMessage());
		}
	}

}
