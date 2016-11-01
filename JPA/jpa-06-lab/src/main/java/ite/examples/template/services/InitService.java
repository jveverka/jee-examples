package ite.examples.template.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ite.examples.data.entities.Assignment;
import ite.examples.data.entities.Plant;
import ite.examples.data.entities.Program;
import ite.examples.data.entities.Project;
import ite.examples.data.entities.User;

@Singleton
@Startup
public class InitService {

	private static final Logger logger = Logger.getLogger(InitService.class.getName());
	
	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	private void init() throws ParseException {
		//doInit();
	}
	
	private void doInit() throws ParseException {
		logger.info("init ...");
		
		List<User> users = new ArrayList<>();
		List<Plant> plants = new ArrayList<>();
		List<Program> programs = new ArrayList<>();
		List<Assignment> assignments = new ArrayList<>();
		List<Project> projects = new ArrayList<>();
		
		User jveverka = new User("jveverka"); users.add(jveverka);
		User sgrossen = new User("sgrossen"); users.add(sgrossen);
		User ltoman = new User("ltoman"); users.add(ltoman);
		User peer = new User("peer"); users.add(peer);
		
		Plant plantZA = new Plant("Zilina"); plants.add(plantZA);
		Plant plantTN = new Plant("Trencin"); plants.add(plantTN);
		Plant plantLU = new Plant("Lucenec"); plants.add(plantLU);
		Plant plantMT = new Plant("Martin"); plants.add(plantMT);
		Plant plantBA = new Plant("Bratislava"); plants.add(plantBA);
		
		Program program01 = new Program("program-01"); programs.add(program01); 
		Program program02 = new Program("program-02"); programs.add(program02);
		Program program03 = new Program("program-03"); programs.add(program03);

		program01.addPlant(plantZA);
		program01.addPlant(plantTN);
		program01.addPlant(plantLU);
		//program01.addPlant(plantMT);
		//program01.addPlant(plantBA);

		program02.addPlant(plantBA);

		program03.addPlant(plantMT);
		
		Assignment as01 = new Assignment("a01", "2015-01-01", "2015-12-12", program01, plantBA, sgrossen, null); assignments.add(as01);
		Assignment as02 = new Assignment("a02", "2015-01-01", "2015-12-12", program02, plantTN, ltoman, null); assignments.add(as02);
		
		Project project01 = new Project("project01", jveverka); projects.add(project01);
		Project project02 = new Project("project02", sgrossen); projects.add(project02);
		Project project03 = new Project("project03", peer); projects.add(project03);
		
		persistAll(users, plants, programs, assignments, projects);

		logger.info("init done.");
	}
	
	private void persistAll(List<User> users, List<Plant> plants, List<Program> programs, List<Assignment> assignments, List<Project> projects) {
		logger.info("persisting users ...");
		for (User user: users) {
			em.persist(user);
		}
		logger.info("persisting plants ...");
		for (Plant plant: plants) {
			em.persist(plant);
		}
		logger.info("persisting programs ...");
		for (Program program: programs) {
			em.persist(program);
		}
		logger.info("persisting assignments ...");
		for (Assignment assignment: assignments) {
			em.persist(assignment);
		}
		logger.info("persisting projects ...");
		for (Project project: projects) {
			em.persist(project);
		}
	}
	
}
