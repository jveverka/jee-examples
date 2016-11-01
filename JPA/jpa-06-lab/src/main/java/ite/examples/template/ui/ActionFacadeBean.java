package ite.examples.template.ui;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.examples.data.entities.User;
import ite.examples.template.services.ActionFacadeService;
import ite.examples.template.services.ProjectService;
import ite.examples.template.services.jdbc.UserDTO;

@ViewScoped
@Named("actions")
public class ActionFacadeBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(ActionFacadeBean.class.getName());
	
	@Inject
	private ActionFacadeService actionFacade;
	
	@Inject 
	private ProjectService projectService;
	
	public void doDataAction() {
		List<UserDTO> result = actionFacade.getUsersAction();
		logger.info("users: [" + result.size() + "]");
	}
	
	public void getUserById() {
		User user = actionFacade.getUserById(4615738L);
		if (user != null) {
			logger.info(user.toString());
			user.setNick("from viewscoped ActionFacadeBean");
		}
	}

	public void getUserByIdAsync() {
		User user = actionFacade.getUserByIdAsync(4615738L);
		if (user != null) {
			logger.info(user.toString());
			user.setNick("from viewscoped ActionFacadeBean");
		}
	}
	
	public void changeProjectOwner() {
		logger.info("changeProjectOwner");
		projectService.changeOwner(4615752L, 4615735L);
	}
	
	public void createProject() {
		logger.info("createProject");
		projectService.createProject("created01");
	}
	
	public void createUsert() {
		logger.info("createUsert");
		actionFacade.createUser();
	}

}
