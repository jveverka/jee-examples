package ite.examples.bv;

import ite.examples.bv.annotations.EMPLOYEE;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Stateless
public class DataProcessingService {
	
	private static final Logger logger = Logger.getLogger(DataProcessingService.class.getName());
	
	@Inject 
	private Validator validator;

	public String createNewDataRecord(@NotNull @Size(min=2, max=10) String name, @NotNull @Min(18) Integer age, @NotNull @Digits(integer=6, fraction=2) Float salary) {
		logger.info("createNewDataRecord: name=" + name + " age=" + age + " salary=" + salary);
		Employee e = new Employee(name, age, salary);
		return "hello: " + e;
	}

	public String createNewDataRecord(@EMPLOYEE Employee e) {
		logger.info("createNewDataRecord: " + e);
		return "hello: " + e;
	}

	public String createNewDataRecordObtainedValidator(String name, Integer age, Float salary) {
		Employee e = new Employee(name, age, salary);
		Set<ConstraintViolation<Employee>> violations = validator.validate(e);
		if (violations.size() != 0) {
			logger.severe("createNewDataRecordValidator: Validation ERRORs detected");
		} else {
			logger.info("createNewDataRecordValidator: name=" + name + " age=" + age + " salary=" + salary);
			return "hello: " + e;
		}
		return "Validation ERROR !";
	}

	public String createNewDataHistoryRecordObtainedValidator(Date hired, Date fired) {
		EmployeeHistory eh = new EmployeeHistory(hired, fired);
		Set<ConstraintViolation<EmployeeHistory>> violations = validator.validate(eh);
		if (violations.size() != 0) {
			logger.severe("createNewDataHistoryRecordObtainedValidator: Validation ERRORs detected");
		} else {
			logger.info("createNewDataHistoryRecordObtainedValidator: eh=" + eh.toString());
			return "HistoryRecord OK: " + eh.toString();
		}
		return "HistoryRecord Validation ERROR: " + eh.toString();
	}

}
