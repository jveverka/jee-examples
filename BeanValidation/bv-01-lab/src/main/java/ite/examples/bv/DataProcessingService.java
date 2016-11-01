package ite.examples.bv;

import java.util.logging.Logger;

import javax.ejb.Stateless;



@Stateless
public class DataProcessingService {
	
	private static final Logger logger = Logger.getLogger(DataProcessingService.class.getName());
	
	public String createNewDataRecord(String name, Integer age, Float salary) {
		logger.info("createNewDataRecord: name=" + name + " age=" + age + " salary=" + salary);
		Employee e = new Employee(name, age, salary);
		return "hello: " + e;
	}

	public String createNewDataRecord(Employee e) {
		logger.info("createNewDataRecord: " + e);
		return "hello: " + e;
	}

}
