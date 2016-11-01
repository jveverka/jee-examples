package ite.examples.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="jpa04_employee")
@NamedEntityGraph(name = "employeeGraph.01",
   attributeNodes = @NamedAttributeNode(value = "jobRecords", subgraph = "jobRecords"),
   subgraphs = @NamedSubgraph(name = "jobRecords", attributeNodes = @NamedAttributeNode("jobTitle")),
   includeAllAttributes = false
)
public class Employee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Basic(fetch=FetchType.LAZY)
	private String firstName;
	@Basic(fetch=FetchType.LAZY)
	private String secondName;
	
	@Temporal(TemporalType.DATE)
	@Basic(fetch=FetchType.LAZY)
	private Date birthDate;

	@Temporal(TemporalType.DATE)
	@Basic(fetch=FetchType.LAZY)
	private Date hiredDate;

	@OneToOne(fetch=FetchType.LAZY)
	private Address address;

	@OneToMany(fetch=FetchType.LAZY)
	private List<JobRecord> jobRecords;
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<Project> projects;
	
	public Employee() {
	}
	
	public Employee(String firstName, String secondName, Date birthDate, Date hiredDate) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.birthDate = birthDate;
		this.hiredDate = hiredDate;
		this.address = null;
		this.jobRecords = new ArrayList<>();
		this.projects = new ArrayList<>();
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<JobRecord> getJobRecords() {
		return jobRecords;
	}

	public void setJobRecords(List<JobRecord> jobRecords) {
		this.jobRecords = jobRecords;
	}
	
	public Date getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public void addJobRecord(JobRecord jobRecord) {
		if (jobRecords == null) {
			jobRecords = new ArrayList<>();
		}
		jobRecords.add(jobRecord);
	}
	
	public void addProject(Project project) {
		if (projects == null) {
			projects = new ArrayList<>();
		}
		projects.add(project);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Employee)) {
			return false;
		}
		if (this.id == null && ((Employee)other).getId() != null) {
			return false;
		}
		if (this.id == null && ((Employee)other).getId() == null) {
			return true;
		}
		return this.id.equals(((Employee)other).getId());
	}
	
}
