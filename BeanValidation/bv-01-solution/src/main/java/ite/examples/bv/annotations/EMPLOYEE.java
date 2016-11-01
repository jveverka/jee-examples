package ite.examples.bv.annotations;

import ite.examples.bv.EmployeeValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {EmployeeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EMPLOYEE {
	
	//Hard coded error message
	String message() default "Invalid Employee instance";

	//Resource bundle key
	//String message() default "{invalid.employee.instance}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
