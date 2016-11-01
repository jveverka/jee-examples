package ite.examples.bv;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Employee {
	
	@NotNull @Size(min=2, max=10)
	private String name;
	@NotNull @Min(18)
	private Integer age;
	@NotNull @Digits(integer=6, fraction=2)
	private Float salary;
	
	public Employee(String name, Integer age, Float salary) {
		super();
		this.name = name;
		this.age = age;
		this.salary = salary;
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
	
	@Override
	public String toString() {
		return "E:" + name + ":" + age + ":" + salary;
	}

}
