package ite.examples.data.entities;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ite.examples.data.entities.listeners.PlantListener;

@Entity
@EntityListeners({PlantListener.class})
@Table(name="jpa06_plant")
public class Plant {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	
	@ManyToOne
	private Program program;
	
	public Plant() {
	}

	public Plant(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Plant) {
			if (id != null) {
				return id.equals(((Plant)other).getId());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Plant [id=" + id + ", name=" + name + ", program=" + program + "]";
	}
	
}
