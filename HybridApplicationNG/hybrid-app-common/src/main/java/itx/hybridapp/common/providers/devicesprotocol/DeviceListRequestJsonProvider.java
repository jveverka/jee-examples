package itx.hybridapp.common.providers.devicesprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.protobuf.util.JsonFormat;

import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListRequest;
import itx.hybridapp.common.providers.AbstractProtocolJsonProvider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceListRequestJsonProvider extends AbstractProtocolJsonProvider<DeviceListRequest> {
	
	private static final Logger logger = Logger.getLogger(DeviceListRequestJsonProvider.class.getName());

	@Override
	public DeviceListRequest readFrom(Class<DeviceListRequest> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		logger.info("read message from JSON format");
		String jsonMessage = CharStreams.toString(new InputStreamReader(entityStream, Charsets.UTF_8));
		DeviceListRequest.Builder builder = DeviceListRequest.newBuilder();
		JsonFormat.parser().merge(jsonMessage, builder);
		return builder.build();
	}

}
