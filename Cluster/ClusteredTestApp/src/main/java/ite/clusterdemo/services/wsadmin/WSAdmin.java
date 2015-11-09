package ite.clusterdemo.services.wsadmin;

import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ite.clusterdemo.services.ConfigData;
import ite.clusterdemo.services.dto.MessageUtils;
import ite.clusterdemo.services.topic.SystemEvent;
import ite.clusterdemo.services.topic.SystemEventPublisher;
import ite.clusterdemo.services.topic.SystemEventType;
import ite.clusterdemo.services.websocket.WsClientGlobalRegistry;

@Path("/wsadmin")
public class WSAdmin {
	
	private final static Logger logger = Logger.getLogger(WSAdmin.class.getName());
	
	@Inject
	private ConfigData config;
	
	@Inject
	private AdminAccessBean adminAccess;
	
	@Inject
	private WsClientGlobalRegistry wsClientGolbalRegistry;
	
	@Inject
	private SystemEventPublisher systemEventPublisher;
	
	@Context 
	private HttpServletRequest request;

	@GET
	@Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String doLoginAction(@QueryParam("userId")String userId, @QueryParam("password")String password) {
		logger.info("doLoginAction http sessionId = " + request.getSession().getId());
		boolean loginResult = adminAccess.doLoginAction(userId, password);
        return MessageUtils.createAdminResponse("adminLogin", loginResult);
	}

	@GET
	@Path("disconnectwssession")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String disconnectWSSession(@QueryParam("wsSessionId")String wsSessionId) {
		logger.info("disconnectWSSession http sessionId = " + request.getSession().getId());
		if (adminAccess.isAuthorized()) {
			SystemEvent systemEvent = new SystemEvent(SystemEventType.sessionDisconnectRequest, wsSessionId);
			systemEventPublisher.publishEvent(systemEvent);
			return MessageUtils.createDisconnectWsSessionResponse(wsSessionId, "REQUEST_ACCEPTED");
		}
		return MessageUtils.createDisconnectWsSessionResponse(wsSessionId, "NOT_AUTHORIZED");
	}

	@GET
	@Path("getadmindata")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAdminData() {
		logger.info("getAdminData http sessionId = " + request.getSession().getId());
		String serverId = config.getServerId();
		String hostName = config.getHostname();
		if (adminAccess.isAuthorized()) {
			String userId = adminAccess.getUserId();
			Date loginTime = adminAccess.getLoginTime();
			return MessageUtils.createAdminDataResponse(userId, loginTime, serverId, hostName, true);
		}
		return MessageUtils.createAdminDataResponse(null, null, serverId, hostName, false);
	}
	
	@GET
	@Path("logout")
    @Produces(MediaType.TEXT_PLAIN)
    public String doLogoutAction() {
		logger.info("doLogoutAction http sessionId = " + request.getSession().getId());
		boolean logoutResult = false;
		if (adminAccess.isAuthorized()) {
			logoutResult = adminAccess.doLogoutAction();
			request.getSession().invalidate();
		}
        return MessageUtils.createAdminResponse("adminLogout", logoutResult);
	}

}
