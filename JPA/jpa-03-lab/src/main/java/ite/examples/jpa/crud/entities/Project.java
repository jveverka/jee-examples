package ite.examples.jpa.crud.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="jpa03_project")
public class Project implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@Enumerated(EnumType.STRING)
	private ProjectState state;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="jpa03_userdata_project", joinColumns={@JoinColumn(name="project_id")}, inverseJoinColumns={@JoinColumn(name="user_id")})
	private List<UserData> users;
	
	public Project() {
		this.id = null;
		this.name = null;
		this.state = ProjectState.CRATED;
	}

	public Project(String name) {
		this.id = null;
		this.name = name;
		this.state = ProjectState.CRATED;
	}

	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ProjectState getState() {
		return state;
	}

	public void setState(ProjectState state) {
		this.state = state;
	}

	public List<UserData> getUsers() {
		return users;
	}

	public void setUsers(List<UserData> users) {
		this.users = users;
	}
	
}
