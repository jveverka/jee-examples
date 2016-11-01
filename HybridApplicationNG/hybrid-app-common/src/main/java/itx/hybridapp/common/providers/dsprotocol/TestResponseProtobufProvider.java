package itx.hybridapp.common.providers.dsprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import itx.hybridapp.common.ProtoMediaType;
import itx.hybridapp.common.protocols.DataServiceProtocol.TestRequest;
import itx.hybridapp.common.providers.AbstractProtocolProtobufProvider;

@Provider
@Produces(ProtoMediaType.APPLICATION_PROTOBUF)
@Consumes(ProtoMediaType.APPLICATION_PROTOBUF)
public class TestResponseProtobufProvider extends AbstractProtocolProtobufProvider<TestRequest> {
	
	private static final Logger logger = Logger.getLogger(TestResponseJsonProvider.class.getName());

	@Override
	public TestRequest readFrom(Class<TestRequest> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		logger.info("read message from PROTOBUF3 format");
		return TestRequest.parseFrom(entityStream);
	}

}
