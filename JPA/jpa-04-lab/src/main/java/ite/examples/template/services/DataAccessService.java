package ite.examples.template.services;

import ite.examples.data.entities.Address;
import ite.examples.data.entities.Employee;
import ite.examples.data.entities.JobRecord;
import ite.examples.data.entities.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DataAccessService {

	private static final Logger logger = Logger.getLogger(DataAccessService.class.getName());
	
	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public Long createEmployeeAction() {
		logger.info("createEmployeeAction ...");
		
		Project javaTrainingProject = new Project("Java Training");
		
		Employee empJuraj = new Employee("Juraj", "Veverka", DataUtils.createDate("20.3.1977"), DataUtils.createDate("1.1.2012"));
		Address jurajsAddress = new Address("Mikoviniho 22", "Zilina", "SK");
		JobRecord jurajsFirstJob = new JobRecord("developer", DataUtils.createDate("1.1.2012"), DataUtils.createDate("1.1.2013"));
		empJuraj.setAddress(jurajsAddress);
		empJuraj.addJobRecord(jurajsFirstJob);
		empJuraj.addProject(javaTrainingProject);
		jurajsFirstJob.setPerson(empJuraj);
		
		javaTrainingProject.addEmployee(empJuraj);
		
		em.persist(jurajsAddress);
		em.persist(jurajsFirstJob);
		em.persist(javaTrainingProject);
		em.persist(empJuraj);
		
		return empJuraj.getId();
	}
	
	public Employee getEmployeeById(Long id) {
		logger.info("getEmployeeById id=" + id);
		EntityGraph<?> graph = em.createEntityGraph("employeeGraph.01");
		Map<String,Object> props = new HashMap<String,Object>();
		props.put("javax.persistence.fetchgraph", graph);
		//props.put("javax.persistence.loadgraph", graph);
		Employee employee = em.find(Employee.class, id, props);
		//Employee employee = em.find(Employee.class, id);
		
		em.detach(employee);
		logger.info("is employee attached: " + em.contains(employee));
		return employee;
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
