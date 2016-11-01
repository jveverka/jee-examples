package ite.online.training.ui.Filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ite.online.training.ui.content.UserAccessBean;

@WebFilter("/content/*")
public class ContentAccessFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger(ContentAccessFilter.class.getName());
	private static final String CONTENT_PREFIX = "/content";
	private static final String CONTENT_LOGIN = "/content/login.xhtml";
	private static final String CONTENT_INDEX_OUT = "/content/index.xhtml?faces-redirect=true";
	private static final String CONTENT_LOGIN_OUT = "/content/login.xhtml?faces-redirect=true";

	@Inject
	private UserAccessBean userAccess;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest req = (HttpServletRequest) request;
        String contextPath = req.getContextPath();
        String requestPath = req.getRequestURI().substring(contextPath.length(), req.getRequestURI().length());
        logger.info("contextPath=" + contextPath + " requestPath=" + requestPath);
        if (userAccess.isValidUser()) {
        	if (requestPath.startsWith(CONTENT_LOGIN) || CONTENT_LOGIN.equals(contextPath)) {
                HttpServletResponse res = (HttpServletResponse) response;
                logger.info("redirecting to: " + contextPath + CONTENT_INDEX_OUT);
                res.sendRedirect(contextPath + CONTENT_INDEX_OUT);
        	} 
        } else {
            if (requestPath.startsWith(CONTENT_PREFIX) && (!requestPath.startsWith(CONTENT_LOGIN) && !CONTENT_LOGIN.equals(requestPath))) {
                HttpServletResponse res = (HttpServletResponse) response;
                logger.info("redirecting to: " + contextPath + CONTENT_LOGIN_OUT);
                res.sendRedirect(contextPath + CONTENT_LOGIN_OUT);
            }
        }
		chain.doFilter(request, response);
    }

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
