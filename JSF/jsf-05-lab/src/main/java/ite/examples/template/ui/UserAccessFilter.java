package ite.examples.template.ui;

import ite.examples.template.services.SessionManagerService;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAccessFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger(UserAccessFilter.class.getName());

	@Inject 
	private SessionManagerService sessionManager;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("filter init "); 
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String originalURL = req.getRequestURL().toString();
		int from = originalURL.trim().lastIndexOf("/");
		String viewId = originalURL.length() > from + 1 ? originalURL.substring(from + 1) : null;
		String sessionId = req.getSession().getId();
		logger.info(originalURL + " viewId=" + viewId + " contextPath: " + req.getContextPath());
		
		if (sessionManager.isValidSession(sessionId)) {
			if (viewId == null || viewId != null && viewId.startsWith("login.xhtml")) {
				res.sendRedirect(req.getContextPath() + "/views/index.xhtml");
			}
			logger.info("valid user with session Id: " + sessionId);
			sessionManager.updateSession(sessionId);
		} else {
			if (viewId == null || viewId != null && !viewId.startsWith("login.xhtml")) {
				res.sendRedirect(req.getContextPath() + "/login.xhtml");
			}
		}
		filterChain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		logger.info("filter destroy "); 
	}

}
