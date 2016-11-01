package ite.examples.jpa.crud.services.projects;

import static ite.examples.jpa.crud.services.QueryRepository.Queries.PROJECTS_GET_ALL;
import ite.examples.jpa.crud.entities.Project;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProjectQueryBean {

	@PersistenceContext
    private EntityManager em;

	public List<Project> getAllProjects() {
		List<Project> projects = em.createNamedQuery(PROJECTS_GET_ALL.name(), Project.class).getResultList();
		return projects;
	}
	
}
