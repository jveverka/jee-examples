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

import ite.online.training.ui.admin.AdminAccessBean;

@WebFilter("/admin/*")
public class AdminAccessFilter implements Filter {

	private static final Logger logger = Logger.getLogger(AdminAccessFilter.class.getName());
	private static final String ADMIN_PREFIX = "/admin";
	private static final String ADMIN_LOGIN = "/admin/login.xhtml";
	private static final String ADMIN_INDEX_OUT = "/admin/index.xhtml?faces-redirect=true";
	private static final String ADMIN_LOGIN_OUT = "/admin/login.xhtml?faces-redirect=true";
	
	@Inject
	private AdminAccessBean adminAccess;
			
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String contextPath = req.getContextPath();
        String requestPath = req.getRequestURI().substring(contextPath.length(), req.getRequestURI().length());
        logger.info("contextPath=" + contextPath + " requestPath=" + requestPath);
        if (adminAccess.isValidUser()) {
        	if (requestPath.startsWith(ADMIN_LOGIN) || ADMIN_LOGIN.equals(contextPath)) {
                HttpServletResponse res = (HttpServletResponse) response;
                logger.info("redirecting to: " + contextPath + ADMIN_INDEX_OUT);
                res.sendRedirect(contextPath + ADMIN_INDEX_OUT);
        	} 
        } else {
            if (requestPath.startsWith(ADMIN_PREFIX) && (!requestPath.startsWith(ADMIN_LOGIN) && !ADMIN_LOGIN.equals(requestPath))) {
                HttpServletResponse res = (HttpServletResponse) response;
                logger.info("redirecting to: " + contextPath + ADMIN_LOGIN_OUT);
                res.sendRedirect(contextPath + ADMIN_LOGIN_OUT);
            }
        }
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
