package ite.examples.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ite.examples.data.entities.listeners.ProgramListener;

@Entity
@EntityListeners({ProgramListener.class})
@Table(name="jpa06_program")
public class Program {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	
	@OneToMany
	private List<Plant> plants;
	
	public Program() {
	}

	public Program(String name) {
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

	public List<Plant> getPlants() {
		return plants;
	}

	public void setPlants(List<Plant> plants) {
		this.plants = plants;
	}
	
	public void addPlant(Plant plant) {
		if (plants == null) {
			this.plants = new ArrayList<>();
		}
		this.plants.add(plant);
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Program) {
			if (id != null) {
				return id.equals(((Program)other).getId());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Program [id=" + id + ", name=" + name + ", plants=" + plants + "]";
	}
	
}
