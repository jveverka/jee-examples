package ite.examples.bv;

import ite.examples.bv.annotations.EMPLOYEE;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmployeeValidator implements ConstraintValidator <EMPLOYEE, Employee> {

	@Override
	public void initialize(EMPLOYEE annotation) {
	}

	@Override
	public boolean isValid(Employee employee, ConstraintValidatorContext context) {
		if (employee != null) {
			if (employee.getName() == null || employee.getName().length() <2 || employee.getName().length() >10) {
				return false;
			}
			if (employee.getAge() == null || employee.getAge() < 18) {
				return false;
			}
			if (employee.getSalary() == null || !(employee.getSalary() < 1000000)) {
				return false;
			}
			return true;
		}
		return false;
	}

}
