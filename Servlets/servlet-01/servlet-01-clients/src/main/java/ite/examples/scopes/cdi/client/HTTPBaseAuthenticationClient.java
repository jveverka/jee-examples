package ite.examples.scopes.cdi.client;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HTTPBaseAuthenticationClient {
	
	private static final Logger logger = Logger.getLogger(HTTPBaseAuthenticationClient.class.getName());
	
	private static final String AUTH_SCOPE = "localhost";
	private static final int AUTH_SCOPE_PORT = 443;
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static final String TARGET_URL = "http://google.com/";
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		logger.info("HTTP BASE AUTH client start ...");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AUTH_SCOPE, AUTH_SCOPE_PORT),
                new UsernamePasswordCredentials(USER_NAME, PASSWORD));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        try {
            HttpGet httpget = new HttpGet(TARGET_URL);

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
		logger.info("HTTP BASE AUTH client done.");
	}


}
