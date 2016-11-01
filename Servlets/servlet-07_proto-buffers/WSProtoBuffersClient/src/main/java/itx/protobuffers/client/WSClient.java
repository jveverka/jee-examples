package itx.protobuffers.client;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import itx.protobuffers.common.UsersProtoc.User;
import itx.protobuffers.common.UsersProtoc.UserList;
import itx.protobuffers.common.providers.ProtoBufferProvider;

public class WSClient {
	
	private static final Logger logger = Logger.getLogger(WSClient.class.getName());
	
	private Configuration config;
	
	public WSClient(Configuration config) {
		this.config = config;
	}
	
	public void getData() {
		try {
			logger.info("****************************************");			
			logger.info("downloading UserList data from: " + config.getServiceURLUserList() );
			long duration = System.currentTimeMillis();
			Client client = ClientBuilder.newBuilder().register(ProtoBufferProvider.class).build(); //.newClient(clientConfig);
			WebTarget target = client.target(config.getServiceURLUserList());
			Builder builder = target.request();
			UserList userList = builder.get(UserList.class);
			
			logger.info("Users: " + userList.getUserCount());
			for (User user: userList.getUserList()) {
				logger.info("User: " + user.toString());
			}
			duration = System.currentTimeMillis() - duration;
			logger.info("finished in: " + duration + "ms");

			logger.info("****************************************");			
			logger.info("downloading User data from: " + config.getServiceURLUser() );
			duration = System.currentTimeMillis();
			target = client.target(config.getServiceURLUser());
			builder = target.request();
			User user = builder.get(User.class);
			logger.info("User: " + user.toString());
			
			logger.info("finished in: " + duration + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
