package itx.hybridapp.server.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import javax.websocket.RemoteEndpoint;
import javax.websocket.SendHandler;

public class MockAsyncRemote implements RemoteEndpoint.Async {

	@Override
	public void setBatchingAllowed(boolean allowed) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getBatchingAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void flushBatch() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPing(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPong(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getSendTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSendTimeout(long timeoutmillis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendText(String text, SendHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Future<Void> sendText(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Void> sendBinary(ByteBuffer data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendBinary(ByteBuffer data, SendHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Future<Void> sendObject(Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendObject(Object data, SendHandler handler) {
		// TODO Auto-generated method stub
		
	}

}
