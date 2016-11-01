package ite.examples.jpa.crud.ui.push;

import ite.examples.jpa.crud.dto.MessageTO;

import java.util.logging.Logger;



import org.primefaces.push.Decoder;

public final class MessageDataMessageDecoder implements Decoder<String, MessageTO> {
	
	private static final Logger logger = Logger.getLogger(MessageDataMessageDecoder.class.getName());

	@Override
	public MessageTO decode(String jsonString) {
		logger.info(jsonString);
		return DataUtils.jsonDecode(jsonString);
	}
}
