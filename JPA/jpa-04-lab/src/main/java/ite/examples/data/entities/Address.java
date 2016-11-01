package ite.examples.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jpa04_address")
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String street;
	private String city;
	private String country;
	
	public Address() {
	}

	public Address(String street, String city, String country) {
		this.street = street;
		this.city = city;
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Address)) {
			return false;
		}
		if (this.id == null && ((Address)other).getId() != null) {
			return false;
		}
		if (this.id == null && ((Address)other).getId() == null) {
			return true;
		}
		return this.id.equals(((Address)other).getId());
	}

}
