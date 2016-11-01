package ite.examples.data.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ite.examples.data.entities.listeners.AssignmentListener;

@Entity
@EntityListeners({ AssignmentListener.class })
@Table(name="jpa06_assignment")
public class Assignment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;

	@Temporal(TemporalType.DATE)
	private Date started;

	@Temporal(TemporalType.DATE)
	private Date finished;

	@OneToOne
	private Program program;
	
	@OneToOne
	private Plant plant;
	
	@OneToOne
	private User addedBy;
	
	@OneToOne
	private User removedBy;
	
	public Assignment() {
	}

	public Assignment(String name, String started, String finished, Program program, Plant plant, User addedBy, User removedBy) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.name = name;
		this.started = sdf.parse(started);
		this.finished = sdf.parse(finished);
		this.program = program;
		this.plant = plant;
		this.addedBy = addedBy;
		this.removedBy = removedBy;
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

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}

	public User getRemovedBy() {
		return removedBy;
	}

	public void setRemovedBy(User removedBy) {
		this.removedBy = removedBy;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Assignment) {
			if (id != null) {
				return id.equals(((Assignment)other).getId());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Assignment [id=" + id + ", name=" + name + ", started=" + started + ", finished=" + finished
				+ ", program=" + program + ", plant=" + plant + ", addedBy=" + addedBy + ", removedBy=" + removedBy
				+ "]";
	}
	
}
