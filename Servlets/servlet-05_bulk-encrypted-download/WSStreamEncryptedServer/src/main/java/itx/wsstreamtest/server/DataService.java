package itx.wsstreamtest.server;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class DataService {
	
	private static final Logger logger = Logger.getLogger(DataService.class.getName());
	
	public void getEncryptedData(String password, int size, long delay, OutputStream output) throws IOException, XMLStreamException, FactoryConfigurationError, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InterruptedException {
		logger.info("getEncryptedData ...");
		generateData(password, size, delay, output);
		output.close();
		logger.info("getEncryptedData done.");
	}
	
	private void generateData(String password, int size, long delay, OutputStream output) throws IOException, XMLStreamException, FactoryConfigurationError, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InterruptedException {
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		byte[] passwdHash = sha.digest(password.getBytes("UTF-8"));
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(passwdHash, 16), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		CipherOutputStream cos = new CipherOutputStream(output, cipher);
		ZipOutputStream zipStream = new ZipOutputStream(cos);
		ZipEntry zipEntry = new ZipEntry("data.xml");
		zipStream.putNextEntry(zipEntry);
		
		XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(zipStream);
		xmlStreamWriter.writeStartDocument();
		xmlStreamWriter.writeStartElement("root");
		int i;
		for (i=0; i<size; i++) {
			xmlStreamWriter.writeStartElement("data");
			xmlStreamWriter.writeAttribute("index", Integer.toString(i));
			xmlStreamWriter.writeCharacters("data: [" + i + "]");
			xmlStreamWriter.writeEndElement();
			if (i%100000 == 0) {
				logger.info("generating record [" + i + "]");
				xmlStreamWriter.flush();
			}
			if (delay > 0) {
				TimeUnit.NANOSECONDS.sleep(delay);
			}
		}
		
		logger.info("data elements send: " + i);
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.close();
		
		zipStream.closeEntry();
		zipStream.close();
		cos.close();
	}

}
