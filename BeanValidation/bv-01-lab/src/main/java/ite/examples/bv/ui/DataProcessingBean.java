package ite.examples.bv.ui;

import ite.examples.bv.DataProcessingService;
import ite.examples.bv.Employee;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ViewScoped
@Named("dataProcessing")
public class DataProcessingBean implements Serializable {

	private static final Logger logger = Logger.getLogger(DataProcessingBean.class.getName());
	
	private String name;
	private Integer age;
	private Float salary;
	private String message;
	
	@Inject
	private DataProcessingService dps;
	
	@PostConstruct
	private void init() {
		logger.info("init...");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Float getSalary() {
		return salary;
	}

	public void setSalary(Float salary) {
		this.salary = salary;
	}

	public String getMessage() {
		return message;
	}
	
	public void dataProcessingAction() {
		message = dps.createNewDataRecord(name, age, salary);
	}

	public void dataProcessingEmployeeValidatorAction() {
		Employee e = new Employee(name, age, salary);
		message = dps.createNewDataRecord(e);
	}

	public void testNullAction() {
		message = dps.createNewDataRecord(null, age, salary);
	}
	
	public void testNullEmployeeValidatorAction() {
		Employee e = new Employee(null, age, salary);
		message = dps.createNewDataRecord(e);
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit...");
	}
	
}
