package itx.hybridapp.server.ws.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import itx.hybridapp.server.services.dto.UserInfo;
import itx.hybridapp.server.services.useraccess.UserAccessService;

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

	private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());
	private static final String LOGIN_URI_ROOT = "/ws/useraccess";
	
	@Inject
	private UserAccessService uaService;
	
	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			String sessionId = request.getSession().getId();
			UserInfo userInfo = uaService.getUserHttpSessionInfo(sessionId);
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new ApplicationSecurityContext(userInfo.getUserName(), userInfo.getRoles(), scheme));
		} catch (Exception e) {
			logger.info("session not authorized");
			String uri = stripOffApplicationName(request.getRequestURI());
			logger.info("URI: " + uri);
			if (uri.startsWith(LOGIN_URI_ROOT)) {
				logger.info("permit login access");
			} else {
				logger.info("FORBIDDEN");
				requestContext.abortWith(Response.status(Status.FORBIDDEN).build());
			}
		}
	}
	
	private String stripOffApplicationName(String uri) {
		logger.info("URI: " + uri);
		if (uri.startsWith("/")) {
			uri = uri.substring(1);
			uri = uri.substring(uri.indexOf('/'), uri.length());
		}
		return uri;
	}

}
