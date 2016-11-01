package ite.examples.ejb.batch.dto;

import java.util.Date;

import ite.examples.ejb.batch.services.BatchResult;

public class BatchJobData {
	
	private Long id;
	private String name;
	private BatchResult result;
	private Date started;
	private Long duration;
	private Integer progress;
	
	public BatchJobData() {
	}
	
	public BatchJobData(Long id, String name, BatchResult result, Date started, Long duration) {
		super();
		this.id = id;
		this.name = name;
		this.result = result;
		this.started = started;
		this.duration = duration;
		this.progress = 0;
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

	public BatchResult getResult() {
		return result;
	}

	public void setResult(BatchResult result) {
		this.result = result;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
}
