package itx.protobuffers.client;

public class Main {

	public static void main(String args[]) {
		Configuration config = Configuration.getInstance();
		WSClient client = new WSClient(config);
		client.getData();
	}

}
