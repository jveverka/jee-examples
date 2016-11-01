package itx.hybridapp.common.client;

public class ClientServiceException extends Exception {
	
	public ClientServiceException() {
		super();
	}

	public ClientServiceException(String message) {
		super(message);
	}

	public ClientServiceException(Exception e) {
		super(e);
	}

	public ClientServiceException(String message, Exception e) {
		super(message, e);
	}

}
