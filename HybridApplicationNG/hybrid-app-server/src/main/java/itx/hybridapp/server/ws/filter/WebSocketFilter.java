package itx.hybridapp.server.ws.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@WebFilter("/ws/wsendpoint")
public class WebSocketFilter implements Filter {
	
	private final static Logger logger = Logger.getLogger(WebSocketFilter.class.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("init ...");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String httpSessionId = httpRequest.getSession().getId();
		String contentType = httpRequest.getContentType();
		final Map<String, String[]> requestParameters = Collections.singletonMap("httpSessionData",
                new String[] { httpSessionId, contentType });
		logger.info("do websocket Filter httpSessionId: " + httpSessionId + " " + contentType);
		HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
            @Override
            public Map<String, String[]> getParameterMap() {
                return requestParameters;
            }
        };
        chain.doFilter(wrappedRequest, response);
	}

	@Override
	public void destroy() {
	}

}
