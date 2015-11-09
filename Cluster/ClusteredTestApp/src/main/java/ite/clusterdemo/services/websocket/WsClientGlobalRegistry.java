package ite.clusterdemo.services.websocket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import ite.clusterdemo.services.dto.SessionInfo;

@Stateless
public class WsClientGlobalRegistry {

	private static final Logger logger = Logger.getLogger(WsClientGlobalRegistry.class.getName());

	@Resource(lookup="java:jboss/infinispan/container/jcacheClustered")
    private CacheContainer container;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public void registerSession(String sessionId, String serverId) {
		logger.info("registerSession: " + sessionId);
		if ("null".equals(sessionId) || sessionId == null || sessionId.length()==0) {
			throw new NullPointerException();
		}
		Cache<String, SessionInfo> cache = container.getCache();
		SessionInfo sessionInfo = new SessionInfo(sessionId, new Date(), serverId); 
		cache.put(sessionId, sessionInfo);
	}
	
	public List<SessionInfo> getActiveSessions() {
		List<SessionInfo> result = new ArrayList<>();
		Cache<String, SessionInfo> cache = container.getCache();
		for (Map.Entry<String, SessionInfo> entry: cache.entrySet()) {
			result.add(entry.getValue());
		}
		logger.info("getActiveSessions[" + result.size() + "]");
		return result;
	}

	public List<SessionInfo> getOtherActiveSessions(String mySessionId) {
		List<SessionInfo> result = new ArrayList<>();
		Cache<String, SessionInfo> cache = container.getCache();
		for (Map.Entry<String, SessionInfo> entry: cache.entrySet()) {
			if (!mySessionId.equals(entry.getValue().getSessionId())) {
				result.add(entry.getValue());
			}
		}
		logger.info("getActiveSessions[" + result.size() + "]");
		return result;
	}

	public boolean isSessionActive(String sessionId) {
		Cache<String, SessionInfo> cache = container.getCache();
		return (cache.get(sessionId) != null);
	}

	public void unregisterSession(String sessionId) {
		logger.info("unregisterSession: " + sessionId);
		Cache<String, SessionInfo> cache = container.getCache();
		cache.remove(sessionId);
	}

}
