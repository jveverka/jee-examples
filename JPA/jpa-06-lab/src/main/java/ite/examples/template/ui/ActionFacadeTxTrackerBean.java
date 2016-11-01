package ite.examples.template.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.examples.data.entities.User;
import ite.examples.template.services.TransAwareService;

@ViewScoped
@Named("actionsTx")
public class ActionFacadeTxTrackerBean implements Serializable {

	private static final Logger logger = Logger.getLogger(ActionFacadeTxTrackerBean.class.getName());
	
	@Inject
	private TransAwareService txService;
	
	public void getUserById() {
		User user = txService.getUserById(4615738L);
		if (user != null) {
			logger.info(user.toString());
			user.setNick("from viewscoped ActionFacadeTxTrackerBean");
		}
	}
	
}
