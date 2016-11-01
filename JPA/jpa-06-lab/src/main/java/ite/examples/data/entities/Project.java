package ite.examples.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ite.examples.data.entities.listeners.ProjectListener;

@Entity
@Table(name="jpa06_project")
@EntityListeners({ProjectListener.class})
public class Project {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	
	@OneToOne //(cascade=CascadeType.REFRESH)
	private User owner;
	
	public Project() {
	}

	public Project(String name, User owner) {
		this.name = name;
		this.owner = owner;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Project) {
			if (id != null) {
				return id.equals(((Project)other).getId());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + "]";
	}
}
