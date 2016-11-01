package itx.wsstreamtest.client;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class WSClient {
	
	private static final Logger logger = Logger.getLogger(WSClient.class.getName());
	
	private Configuration config;
	
	public WSClient(Configuration config) {
		this.config = config;
	}
	
	public void getData() {
		try {
			logger.info("downloading data from: " + config.getServiceURL() );
			long duration = System.currentTimeMillis();
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(config.getServiceURL());
			
			logger.info("reading plain stream ...");
			InputStream inputStream = target.request(MediaType.APPLICATION_OCTET_STREAM).get(InputStream.class);
			
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] passwdHash = sha.digest(config.getPassword().getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(passwdHash, 16), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			logger.info("opening cipher input stream ...");
			CipherInputStream cis = new CipherInputStream(inputStream, cipher);
			
			logger.info("opening zip stream:");
			ZipInputStream zif = new ZipInputStream(cis);

			ZipEntry entry = zif.getNextEntry();
			
			int dataCounter = 0;

			while (entry != null) {
				logger.info("zip entry: " + entry.getName());
				
				logger.info("opening xmlEventReader:");
				XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(zif);
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (event.isStartElement() && "data".equals(event.asStartElement().getName().toString())) {
						dataCounter++;
					}
					if (dataCounter%10000 == 0) {
						logger.info("received events [" + dataCounter + "]");
					}
					//logger.info("XML: " + event.toString());
				}
				//eventReader.close();
				//Scanner sc = new Scanner(zif);
				//while(sc.hasNextLine()) {
				//	logger.info("XML: " + sc.nextLine());
				//}
				//entry = zif.getNextEntry();
				entry = null;
				//sc.close();
			}
			
			logger.info("received data events: " + dataCounter);
			logger.info("closing all streams");
			zif.close();
			cis.close();
			inputStream.close();
			duration = System.currentTimeMillis() - duration;
			logger.info("finished in: " + duration + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
