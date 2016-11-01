package ite.example.services.ws;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import ite.example.services.ws.dto.ResponseMessage;

public class MessageEncoder implements Encoder.Text<ResponseMessage> {

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig epConfig) {
	}

	@Override
	public String encode(ResponseMessage responseMessage) throws EncodeException {
		return responseMessage.toJsonString();
	}

}
