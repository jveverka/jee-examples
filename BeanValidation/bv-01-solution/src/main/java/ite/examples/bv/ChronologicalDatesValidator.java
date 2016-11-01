package ite.examples.bv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ChronologicalDatesValidator implements ConstraintValidator <ChronologicalDates, EmployeeHistory> {

	@Override
	public void initialize(ChronologicalDates constraintAnnotation) {
	}

	@Override
	public boolean isValid(EmployeeHistory value, ConstraintValidatorContext context) {
		if (value.getHired().before(value.getFired())) {
			return true;
		}
		return false;
	}

}
