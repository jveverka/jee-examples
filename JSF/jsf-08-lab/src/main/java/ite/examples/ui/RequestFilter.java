package ite.examples.ui;

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

import ite.examples.services.access.UserAccessService;

public class RequestFilter implements Filter {

	private static final Logger logger = Logger.getLogger(RequestFilter.class.getName());

    private FilterConfig filterConfig;

	@Inject
	private UserAccessService userService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
		
        if (!userService.isAuthorized()) {
            logger.info("Redirect to /login.xhtml, user " + userService.getUserName() + " is not authorized !");
            res.sendRedirect(req.getContextPath() + "/login.xhtml");        	
        } 
        chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}

}
