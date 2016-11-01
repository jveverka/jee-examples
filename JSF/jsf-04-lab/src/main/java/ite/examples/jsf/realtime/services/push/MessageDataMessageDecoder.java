package ite.examples.jsf.realtime.services.push;

import java.util.logging.Logger;


import org.primefaces.push.Decoder;

public final class MessageDataMessageDecoder implements Decoder<String, EventMessage> {
	
	private static final Logger logger = Logger.getLogger(MessageDataMessageDecoder.class.getName());

	@Override
	public EventMessage decode(String jsonString) {
		//logger.info(jsonString);
		return DataUtils.jsonDecode(jsonString);
	}
}
