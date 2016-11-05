package itx.examples.swagger.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;

import itx.examples.swagger.ws.dto.ApplicationInfo;
import itx.examples.swagger.ws.dto.DataResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientTests {
	
	@Test
	public void testGetInfo() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target("http://localhost:8080/servlet-09_jaxrs-swagger/ws/swaggers/info");
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		ApplicationInfo ai = builder.get(ApplicationInfo.class);
		Assert.assertNotNull(ai);
		Assert.assertEquals(ai.getName(), "wildfly-swagger-demo");
		Assert.assertEquals(ai.getVersion(), "1.0");
	}

	@Test
	public void testGetData() {
		String data = "xxxx";
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target("http://localhost:8080/servlet-09_jaxrs-swagger/ws/swaggers/data?data=" + data);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		DataResponse dr = builder.get(DataResponse.class);
		Assert.assertNotNull(dr);
		Assert.assertEquals(dr.getData(), data.toUpperCase());
	}
	
	@Test
	public void testSwaggerJson() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target("http://localhost:8080/servlet-09_jaxrs-swagger/ws/swagger.json");
		Builder builder = target.request();
		Response response = builder.get();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus() == 200);
		JsonElement jsonElement = new JsonParser().parse(response.readEntity(String.class));
		Assert.assertNotNull(jsonElement);
		String version = jsonElement.getAsJsonObject().get("swagger").getAsString();
		Assert.assertNotNull(version);
		Assert.assertEquals(version, "2.0");
	}

}
