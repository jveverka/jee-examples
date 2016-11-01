package itx.hybridapp.common.client.test;

import javax.ws.rs.core.MediaType;

import org.testng.annotations.DataProvider;

import itx.hybridapp.common.ProtoMediaType;

public abstract class UserAccessTest {

	@DataProvider(name = "loginLogoutProvider")
	public static Object[][] loginLogoutProvider() {
		return new Object[][] {
			{ "user", "user123", new String[]{ "user" }, ProtoMediaType.APPLICATION_PROTOBUF },
			{ "demo", "demo123", new String[]{ "user" }, ProtoMediaType.APPLICATION_PROTOBUF },
			{ "admin", "admin123", new String[]{ "user", "admin" }, ProtoMediaType.APPLICATION_PROTOBUF },
			{ "root", "root123", new String[]{ "user", "admin" }, ProtoMediaType.APPLICATION_PROTOBUF },
			{ "user", "user123", new String[]{ "user" }, MediaType.APPLICATION_JSON },
			{ "demo", "demo123", new String[]{ "user" }, MediaType.APPLICATION_JSON },
			{ "admin", "admin123", new String[]{ "user", "admin" }, MediaType.APPLICATION_JSON },
			{ "root", "root123", new String[]{ "user", "admin" }, MediaType.APPLICATION_JSON },
		};
	}

	@DataProvider(name = "invalidLoginProvider")
	public static Object[][] invalidLoginProvider() {
		return new Object[][] {
			{ "user", "user123xx", ProtoMediaType.APPLICATION_PROTOBUF },
			{ "demo", "demo123xx", ProtoMediaType.APPLICATION_PROTOBUF },
			{ "admin", "admin123xx", ProtoMediaType.APPLICATION_PROTOBUF },
			{ "root", "root123xx", ProtoMediaType.APPLICATION_PROTOBUF },
			{ "user", "user123xx", MediaType.APPLICATION_JSON },
			{ "demo", "demo123xx", MediaType.APPLICATION_JSON },
			{ "admin", "admin123xx", MediaType.APPLICATION_JSON },
			{ "root", "root123xx", MediaType.APPLICATION_JSON },
		};
	}
	
}
