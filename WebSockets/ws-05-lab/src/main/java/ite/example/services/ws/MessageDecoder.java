package ite.example.services.ws;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import ite.example.services.ws.dto.RequestMessage;

public class MessageDecoder implements Decoder.Text<RequestMessage> {

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig epConfig) {
	}

	@Override
	public RequestMessage decode(String stringMessage) throws DecodeException {
		JsonReader reader = Json.createReader(new StringReader(stringMessage));
		JsonObject obj = reader.readObject();
		RequestMessage request = new RequestMessage();
		request.setMessage(obj.getString("message"));
		request.setId(obj.getJsonNumber("id").longValue());
		request.setTimestamp(obj.getJsonNumber("timestamp").longValue());
		return request;
	}

	@Override
	public boolean willDecode(String stringMessage) {
		return (stringMessage != null);
	}

}
