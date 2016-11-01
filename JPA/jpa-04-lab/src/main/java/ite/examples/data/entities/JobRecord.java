package ite.examples.data.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="jpa04_jobrecord")
public class JobRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String jobTitle;
	@Temporal(TemporalType.DATE)
	private Date started;
	@Temporal(TemporalType.DATE)
	private Date finished;
	
	@ManyToOne
	private Employee person;
	
	public JobRecord() {
	}

	public JobRecord(String jobTitle, Date started, Date finished) {
		this.jobTitle = jobTitle;
		this.started = started;
		this.finished = finished;
		this.person = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}
	
	public Employee getPerson() {
		return person;
	}

	public void setPerson(Employee person) {
		this.person = person;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof JobRecord)) {
			return false;
		}
		if (this.id == null && ((JobRecord)other).getId() != null) {
			return false;
		}
		if (this.id == null && ((JobRecord)other).getId() == null) {
			return true;
		}
		return this.id.equals(((JobRecord)other).getId());
	}

}
