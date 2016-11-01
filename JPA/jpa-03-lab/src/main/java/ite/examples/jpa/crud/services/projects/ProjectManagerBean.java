package ite.examples.jpa.crud.services.projects;

import ite.examples.jpa.crud.entities.Project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProjectManagerBean {

	@PersistenceContext
    private EntityManager em;
	
	public void createProject(String name) {
		Project project = new Project(name);
		em.persist(project);
	}
	
	public void deleteProject(Long id) {
		Project project = em.find(Project.class, id);
		em.remove(project);
	}
	
}
