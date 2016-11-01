package ite.examples.jpa.crud.ui;

import ite.examples.jpa.crud.entities.Project;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named("projectview")
public class ProjectViewClientBean {
	
	private List<Project> projects;
	private Long id;
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	

}
