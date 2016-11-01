package ite.examples.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ite.examples.data.entities.listeners.UserListener;

@Entity
@EntityListeners({UserListener.class})
@Table(name="jpa06_user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	
	@Column(updatable=false, insertable=true)
	private String nick;
	
	@OneToMany
	private List<Project> projects;
	
	public User() {
	}

	public User(String name) {
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

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project project) {
		if (projects == null) {
			projects = new ArrayList<>();
		}
		projects.add(project);
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof User) {
			if (id != null) {
				return id.equals(((User)other).getId());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", nick=" + nick + "]";
	}

}
