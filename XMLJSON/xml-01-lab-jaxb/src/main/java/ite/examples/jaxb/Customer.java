package ite.examples.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {

	String name;
	String surname;
	int age;
	int id;
 
	public Customer() {
	}
	
	public Customer(int id, String name, String surname, int age) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
 
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
 
	public int getAge() {
		return age;
	}
 
	@XmlElement
	public void setAge(int age) {
		this.age = age;
	}
 
	public int getId() {
		return id;
	}
 
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	@XmlElement
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Override
	public String toString() {
		return "CUSTOMER:" + name + ":" + age + ":" + surname;
	}
	
}
