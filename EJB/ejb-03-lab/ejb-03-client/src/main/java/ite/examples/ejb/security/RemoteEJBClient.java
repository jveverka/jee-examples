package ite.examples.ejb.security;


import ite.examples.ejb.security.BackendService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;


public class RemoteEJBClient {
	
	private static final Logger logger = Logger.getLogger(RemoteEJBClient.class.getName());
	private static final String EJBGUEST  = "ejbguest";
	private static final String EJBUSER  = "ejbuser";
	private static final String EJBADMIN  = "ejbadmin";
	
	public static void main(String[] args) {
		logger.info("External Client start ...");
		Map<String,String> credentials = new HashMap<>();
		credentials.put(EJBGUEST, "ejbguest123");
		credentials.put(EJBUSER , "ejbuser123" );
		credentials.put(EJBADMIN, "ejbadmin123");
		
		//String currentUser = EJBGUEST;
		//String currentUser = EJBUSER;
		String currentUser = EJBADMIN;
		
		try {
			long start = System.currentTimeMillis();
			BackendService backendService = lookupStatelessRemoteEJB(currentUser, credentials.get(currentUser));
			long lookupDuration = System.currentTimeMillis() - start;
			logger.info("Lookup duration: " + lookupDuration + "ms");		
			long startExec = System.currentTimeMillis();
			logger.info("PD: " + backendService.getPublicData(currentUser));
			try {
				logger.info("UD: " + backendService.getUserData(currentUser));
			} catch (EJBAccessException e) {
				logger.severe("UD: ACCESS DENIED !");
			}
			try {
				logger.info("AD: " + backendService.getAdminData(currentUser));
			} catch (EJBAccessException e) {
				logger.severe("AD: ACCESS DENIED !");
			}
			long callDuration = System.currentTimeMillis() - startExec;
			logger.info("Exec duration: " + callDuration + "ms");
		} catch (NamingException e) {
			logger.log(Level.SEVERE, "NamingException", e);
		}
		logger.info("External Client done.");
	}

	private static BackendService lookupStatelessRemoteEJB(String userName, String password) throws NamingException {
		final Properties clientProp = new Properties();
		clientProp.put("endpoint.name", "client-endpoint");
		clientProp.put("remote.connections" , "default");
		clientProp.put("remote.connection.default.host", "localhost");
		clientProp.put("remote.connection.default.port", "8080");
		clientProp.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		clientProp.put("remote.connection.default.username", userName);
		clientProp.put("remote.connection.default.password", password);
		clientProp.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(clientProp);
		ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);
		EJBClientContext.setSelector(selector);
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		final Context context = new InitialContext(jndiProperties);
		BackendService bean = (BackendService)context.lookup("ejb:/ejb-03-lab/BackendServiceBeanImpl!ite.examples.ejb.security.BackendServiceRemote?stateful");
		return bean;
	}

}
