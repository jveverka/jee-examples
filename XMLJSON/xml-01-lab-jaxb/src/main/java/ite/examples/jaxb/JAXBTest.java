package ite.examples.jaxb;


import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class JAXBTest {
	
	private static final Logger logger = Logger.getLogger(JAXBTest.class.getName());
	
	public static void main(String[] args) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);

		logger.info("JAXBTest: unmarshalling ... ");
		InputStream is = System.class.getResourceAsStream("/ite/examples/jaxb/Customer.xml");
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Customer customer = (Customer)unmarshaller.unmarshal(is);
		logger.info(customer.toString());
		logger.info("JAXBTest: marshalling ... ");
		customer = new Customer(5, "XX", "YYY", 12);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //pretty printed
		jaxbMarshaller.marshal(customer, System.out);
		logger.info("JAXBTest: done.");
	}

}
