package itx.hybridapp.server.services.test;

import java.util.ArrayList;
import java.util.List;

import itx.hybridapp.common.protocols.CommonAccessProtocol.EchoStructuredPayload;

public final class TestUtils {
	
	public static List<EchoStructuredPayload> getStructuredPayload(int length) {
		List<EchoStructuredPayload> result = new ArrayList<>(length);
		boolean active = true;
		float value = 0;
		for (int i=0; i<length; i++) {
			EchoStructuredPayload esp = EchoStructuredPayload.newBuilder()
					.setTimestamp(System.currentTimeMillis())
					.setMessage("message: " + i)
					.setValue(value)
					.setActive(active)
					.build();
			active = !active;
			value = value + 0.1f;
			result.add(esp);
		}
		return result;
	}

}
