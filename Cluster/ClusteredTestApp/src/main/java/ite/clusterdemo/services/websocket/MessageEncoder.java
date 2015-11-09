package ite.clusterdemo.services.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.dto.MessageUtils;

public class MessageEncoder implements Encoder.Text<DataMessage> {

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig epConfig) {
	}

	@Override
	public String encode(DataMessage responseMessage) throws EncodeException {
		return MessageUtils.encode(responseMessage);
	}

}
