package ite.examples.jpa.crud.services;

import java.util.logging.Logger;

import ite.examples.jpa.crud.entities.Project;
import ite.examples.jpa.crud.entities.UserData;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Singleton
@Startup
public class QueryRepository {
	
	private static final Logger logger = Logger.getLogger(QueryRepository.class.getName());
	
	public enum Queries {
		USERS_GET_ALL,
		PROJECTS_GET_ALL,
	}
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext
	private EntityManager entityManager;
	 
	@PostConstruct
	public void init() {
		logger.info("init ...");
		entityManagerFactory.addNamedQuery(Queries.USERS_GET_ALL.name(), buildGetUsersAll());
		entityManagerFactory.addNamedQuery(Queries.PROJECTS_GET_ALL.name(), buildGetProjectsAll());
	}
	
	private TypedQuery<UserData> buildGetUsersAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserData> criteriaQuery = criteriaBuilder.createQuery(UserData.class);
		Root<UserData> root = criteriaQuery.from(UserData.class);
		criteriaQuery.select(root);
		return entityManager.createQuery(criteriaQuery);		
	}

	private TypedQuery<Project> buildGetProjectsAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
		Root<Project> root = criteriaQuery.from(Project.class);
		criteriaQuery.select(root);
		return entityManager.createQuery(criteriaQuery);		
	}

}
