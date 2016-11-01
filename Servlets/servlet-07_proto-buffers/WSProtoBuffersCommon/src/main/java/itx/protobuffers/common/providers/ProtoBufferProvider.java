package itx.protobuffers.common.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import itx.protobuffers.common.UsersProtoc.User;
import itx.protobuffers.common.UsersProtoc.UserList;

@Provider
@Produces("application/protobuf")
@Consumes("application/protobuf")
public class ProtoBufferProvider implements MessageBodyReader, MessageBodyWriter {
	
	private static final Logger logger = Logger.getLogger(ProtoBufferProvider.class.getName());

	@Override
	public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return User.class.isAssignableFrom(type) 
                || UserList.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(Object t, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		logger.info("getSize");
		return 0;
	}

	@Override
	public void writeTo(Object t, Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		logger.info("writeTo");
        if (t instanceof User) {
    		logger.info("writing user ...");
            User user = (User)t;
            user.writeTo(entityStream);
        } else if (t instanceof UserList) {
    		logger.info("writing userList ...");
            UserList list = (UserList)t;
            list.writeTo(entityStream);
        }
		logger.info("writing done.");
        entityStream.flush();
    }

	@Override
	public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		boolean result = User.class.isAssignableFrom(type) 
                || UserList.class.isAssignableFrom(type);
		logger.info("isReadable: " + result);
		return result;
	}

	@Override
	public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		logger.info("readFrom");
		if (User.class.isAssignableFrom(type)) {
			logger.info("reading user");
            return User.parseFrom(entityStream);
        } else if (UserList.class.isAssignableFrom(type)) {
			logger.info("reading userList");
            return UserList.parseFrom(entityStream);
        } else {
            throw new BadRequestException("Can't Deserailize");
        }
	}

}
