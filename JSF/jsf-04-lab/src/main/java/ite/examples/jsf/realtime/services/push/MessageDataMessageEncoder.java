package ite.examples.jsf.realtime.services.push;

import java.util.logging.Logger;

import org.primefaces.push.Encoder;


public final class MessageDataMessageEncoder implements Encoder<EventMessage, String> {
	
	private static final Logger logger = Logger.getLogger(MessageDataMessageEncoder.class.getName());
 
    @Override
    public String encode(EventMessage message) {
    	String strMessage = DataUtils.jsonEncode(message);
    	//logger.info(strMessage);
        return strMessage;
    }
    
}
