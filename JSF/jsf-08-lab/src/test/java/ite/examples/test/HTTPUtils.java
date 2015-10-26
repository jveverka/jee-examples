package ite.examples.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.testng.Assert;

public final class HTTPUtils {

	public static String callPOST(String targetURL, String requestData, String sessionId) {
		try {
			URL url = new URL(targetURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.setDoOutput(true);
			connection.setReadTimeout(6000);
			if (sessionId != null) { 
				connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
			}
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(requestData);
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			String responseStr = "";
		   	while ((line = rd.readLine()) != null) {
		   		responseStr = responseStr + line;
		   	}
		   	wr.close();
		   	rd.close();
		   	return responseStr;
		} catch (MalformedURLException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		return null;
	}
	
}
