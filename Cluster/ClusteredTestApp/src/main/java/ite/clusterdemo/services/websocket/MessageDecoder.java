package ite.clusterdemo.services.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import ite.clusterdemo.services.dto.DataMessage;
import ite.clusterdemo.services.dto.MessageUtils;

public class MessageDecoder implements Decoder.Text<DataMessage> {

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig epConfig) {
	}

	@Override
	public DataMessage decode(String stringMessage) throws DecodeException {
		return MessageUtils.decode(stringMessage);
	}

	@Override
	public boolean willDecode(String stringMessage) {
		return (stringMessage != null);
	}

}
