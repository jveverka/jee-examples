package ite.examples.bv;

public class Employee {
	
	private String name;
	private Integer age;
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
