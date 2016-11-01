package ite.examples.jpa.crud.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserData {
	
	@Id
	private String id;
	private String firstName;
	private String secondName;
	private String phone;
	private String email;
	
	public UserData() {
		this.id = UUID.randomUUID().toString();
	}
	
	public UserData(String firstName, String secondName, String email, String phone) {
		super();
		this.id = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
