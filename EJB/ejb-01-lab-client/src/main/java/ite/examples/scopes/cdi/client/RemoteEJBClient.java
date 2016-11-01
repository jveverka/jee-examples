package ite.examples.scopes.cdi.client;


import ite.examples.scopes.cdi.StatefulRemote;
import ite.examples.scopes.cdi.StatelessRemote;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class RemoteEJBClient {
	
	private static final Logger logger = Logger.getLogger(RemoteEJBClient.class.getName());
	
	public static void main(String[] args) {
		logger.info("External Client start ...");
		try {
			long start = System.currentTimeMillis();
			StatefulRemote statefulRemote = lookupStatefulRemoteEJB();
			StatelessRemote statelessRemote = lookupStatelessRemoteEJB();
			long lookupDuration = System.currentTimeMillis() - start;
			logger.info("Lookup duration: " + lookupDuration + "ms");		
			long startExec = System.currentTimeMillis();
			logger.info("StatefulRemote id=" + statefulRemote.getId());
			logger.info("StatefulRemote name=" + statefulRemote.getName());
			logger.info("StatelessRemote id=" + statelessRemote.getId());
			logger.info("StatelessRemote name=" + statelessRemote.getName());
			long callDuration = System.currentTimeMillis() - startExec;
			logger.info("Exec duration: " + callDuration + "ms");
		} catch (NamingException e) {
			logger.log(Level.SEVERE, "NamingException", e);
		}
		logger.info("External Client done.");
	}

	private static StatefulRemote lookupStatefulRemoteEJB() throws NamingException {
		final Hashtable<String,String> jndiProperties = new Hashtable<>();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		final Context context = new InitialContext(jndiProperties);
		StatefulRemote bean = (StatefulRemote)context.lookup("ejb:/ejb-01-lab/StatefulBean!ite.examples.scopes.cdi.StatefulRemote?stateful");
		return bean;
	}

	private static StatelessRemote lookupStatelessRemoteEJB() throws NamingException {
		final Hashtable<String,String> jndiProperties = new Hashtable<>();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		final Context context = new InitialContext(jndiProperties);
		StatelessRemote bean = (StatelessRemote)context.lookup("ejb:/ejb-01-lab/StatelessBean!ite.examples.scopes.cdi.StatelessRemote");
		return bean;
	}

}
