package ite.examples.jpa.crud.ui.push;

import ite.examples.jpa.crud.dto.MessageTO;

import java.util.logging.Logger;

import org.primefaces.push.Encoder;


public final class MessageDataMessageEncoder implements Encoder<MessageTO, String> {
	
	private static final Logger logger = Logger.getLogger(MessageDataMessageEncoder.class.getName());
 
    @Override
    public String encode(MessageTO message) {
    	String strMessage = DataUtils.jsonEncode(message);
    	logger.info(strMessage);
        return strMessage;
    }
    
}
