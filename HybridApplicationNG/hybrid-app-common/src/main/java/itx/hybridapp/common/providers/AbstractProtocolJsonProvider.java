package itx.hybridapp.common.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.google.common.base.Charsets;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

public abstract class AbstractProtocolJsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<Message> {
	
	private static final Logger logger = Logger.getLogger(AbstractProtocolJsonProvider.class.getName());

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Message.class.isAssignableFrom(type); 
	}

	@Override
	public long getSize(Message t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(Message t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		logger.info("write message in JSON format");
        if (t instanceof Message) {
    		Message protoMessage = (Message)t;
    		String jsonData = JsonFormat.printer().includingDefaultValueFields().print(protoMessage);
    		entityStream.write(jsonData.getBytes(Charsets.UTF_8));
        } else {
            throw new BadRequestException("Unable to write: expected protobuf messsage type");
        }
        entityStream.flush();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Message.class.isAssignableFrom(type);
	}

	public abstract T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException;

}
