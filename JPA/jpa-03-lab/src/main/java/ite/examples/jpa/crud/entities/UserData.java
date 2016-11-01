package ite.examples.jpa.crud.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="jpa03_userdata")
public class UserData implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String secondName;
	private String phone;
	private String email;
	
	@Transient
	private List<Label> labels;
	
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="jpa03_userdata_project", joinColumns={@JoinColumn(name="user_id")}, inverseJoinColumns={@JoinColumn(name="project_id")})
	private List<Project> projects;
	
	public UserData() {
	}
	
	public UserData(String firstName, String secondName, String email, String phone) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
}
