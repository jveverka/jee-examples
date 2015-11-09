package ite.clusterdemo.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ConfigData {
	
	private static final Logger logger = Logger.getLogger(ConfigData.class.getName());
	private static final String WF_SERVER_ID = "wildfly.serverid";
	
	private String hostname;
	private String serverId;
	
	@PostConstruct
	private void init() {
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			serverId = System.getProperty(WF_SERVER_ID);
			logger.info("config: hostname=" + hostname + " serverId=" + serverId);
		} catch (UnknownHostException e) {
			hostname = "unknown";
		}
	}

	public String getHostname() {
		return hostname;
	}

	public String getServerId() {
		return serverId;
	}

}
