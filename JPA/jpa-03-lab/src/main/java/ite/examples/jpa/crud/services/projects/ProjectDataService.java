package ite.examples.jpa.crud.services.projects;

import ite.examples.jpa.crud.entities.Project;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ProjectDataService {

	@Inject
	private ProjectQueryBean projectQuery;
	
	public List<Project> getAllProjects() {
		return projectQuery.getAllProjects();
	}
	
}
