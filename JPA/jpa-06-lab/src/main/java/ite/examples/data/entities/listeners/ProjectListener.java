package ite.examples.data.entities.listeners;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import ite.examples.data.entities.Project;
import ite.examples.template.services.ProjectService;

public class ProjectListener {

	private static final Logger logger = Logger.getLogger(ProjectListener.class.getName());
	
	//@Inject
	//private ProjectService projectService;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

	@PrePersist
	public void prePersist(Project project) {
		logger.info("prePersist: " + project.toString());
	}
	
	@PostPersist 
	public void postPersist(Project project) {
		logger.info("postPersist: " + project.toString());
		try {
			InitialContext ctx = new InitialContext();
			BeanManager bm = (BeanManager)ctx.lookup("java:comp/BeanManager");
			Set<Bean<?>> beans = bm.getBeans(ProjectService.class);
			Bean<?> bean = bm.resolve(beans);
			CreationalContext<?> context = bm.createCreationalContext(bean);
			ProjectService projectService = (ProjectService) bm.getReference(bean, ProjectService.class, context);
			projectService.onPostPersistAsync();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "postPersist failed", e);
		}
	}
	
	@PreRemove
	public void preRemove(Project project) {
		logger.info("preRemove: " + project.toString());
	}
	
	@PostRemove
	public void postRemove(Project project) {
		logger.info("postRemove: " + project.toString());
	}
	
	@PreUpdate
	public void preUpdate(Project project) {
		logger.info("preUpdate: " + project.toString());
	}
	
	
	@PostUpdate 
	public void postUpdate(Project project) {
		logger.info("postUpdate: " + project.toString());
	}
	
	@PostLoad
	public void postLoad(Project project) {
		logger.info("postLoad: " + project.toString());
		try {
			InitialContext ctx = new InitialContext();
			BeanManager bm = (BeanManager)ctx.lookup("java:comp/BeanManager");
			Set<Bean<?>> beans = bm.getBeans(ProjectService.class);
			Bean<?> bean = bm.resolve(beans);
			CreationalContext<?> context = bm.createCreationalContext(bean);
			ProjectService projectService = (ProjectService) bm.getReference(bean, ProjectService.class, context);
			projectService.onLoadAsync();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "postLoad failed", e);
		}
	}

}
