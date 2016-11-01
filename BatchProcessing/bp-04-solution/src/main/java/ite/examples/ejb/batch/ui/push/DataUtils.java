package ite.examples.ejb.batch.ui.push;

import ite.examples.ejb.batch.dto.MessageTO;

import java.io.StringReader;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

public class DataUtils {
	
	private static final String messageJS = "message";
	
	public static String jsonEncode(MessageTO message) {
		StringWriter writer = new StringWriter();
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add(messageJS, message.getMessage());
		JsonWriter jsonWriter = Json.createWriter(writer);
		jsonWriter.writeObject(jsonBuilder.build());
		return writer.toString();
	}

	public static MessageTO jsonDecode(String jsonString) {
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonObject jsonObject = jsonReader.readObject();
		String eventType = jsonObject.getString(messageJS);
		return new MessageTO(eventType);		
	}

	public static void main(String[] args) {
		MessageTO em = new MessageTO("xxx");
		String emJsonString = jsonEncode(em);
		System.out.println("ENCODED: " + emJsonString);
		MessageTO emDec = jsonDecode(emJsonString);
		String emDecoded = jsonEncode(emDec);
		System.out.println("DECODED: " + emDecoded);
		
	}
	
}
