package itx.hybridapp.common.providers.devicesprotocol;

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
import itx.hybridapp.common.protocols.DeviceServiceProtocol.DeviceListResponse;
import itx.hybridapp.common.providers.AbstractProtocolProtobufProvider;

@Provider
@Produces(ProtoMediaType.APPLICATION_PROTOBUF)
@Consumes(ProtoMediaType.APPLICATION_PROTOBUF)
public class DeviceListResponseProtobufProvider extends AbstractProtocolProtobufProvider<DeviceListResponse> {
	
	private static final Logger logger = Logger.getLogger(DeviceListResponseProtobufProvider.class.getName());

	@Override
	public DeviceListResponse readFrom(Class<DeviceListResponse> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		logger.info("read message from PROTOBUF3 format");
		return DeviceListResponse.parseFrom(entityStream);
	}

}
