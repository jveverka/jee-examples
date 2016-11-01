package ite.examples.mdb.remoteclient;


import ite.examples.mdb.MessageTO;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class RemoteMDBQueueProducer {
	
	private static final Logger logger = Logger.getLogger(RemoteMDBQueueProducer.class.getName());
	
	private static final String DEFAULT_MESSAGE = "Hello from remote MDB Queue client.";
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/queue/TestQueue";
	private static final String DEFAULT_USERNAME = "jmsuser";
	private static final String DEFAULT_PASSWORD = "jmsuser123";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
	
	public static void main(String[] args) {
		Context namingContext = null;
		try {
			// Set up the namingContext for the JNDI lookup
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
			env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
			env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);
			namingContext = new InitialContext(env);
			// Perform the JNDI lookups
			logger.info("Attempting to acquire connection factory \"" + DEFAULT_CONNECTION_FACTORY + "\"");
			ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(DEFAULT_CONNECTION_FACTORY);
			logger.info("Found connection factory \"" + DEFAULT_CONNECTION_FACTORY	+ "\" in JNDI");
			logger.info("Attempting to acquire destination \"" + DEFAULT_DESTINATION	+ "\"");
			Destination destination = (Destination) namingContext.lookup(DEFAULT_DESTINATION);
			logger.info("Found destination \"" + DEFAULT_DESTINATION + "\" in JNDI");
			try (JMSContext context = connectionFactory.createContext(DEFAULT_USERNAME,	DEFAULT_PASSWORD)) {
				MessageTO mto = new MessageTO(DEFAULT_MESSAGE);
				logger.info("Sending message with content: " + mto.getMessage());
				context.createProducer().send(destination, mto);
			}
		} catch (NamingException e) {
			logger.log(Level.SEVERE,"", e);
		} finally {
			if (namingContext != null) {
				try {
					namingContext.close();
				} catch (NamingException e) {
					logger.log(Level.SEVERE,"", e);
				}
			}
		}
	}
}
