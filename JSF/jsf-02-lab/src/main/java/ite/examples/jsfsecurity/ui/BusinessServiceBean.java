package ite.examples.jsfsecurity.ui;

import ite.examples.jsfsecurity.services.BusinessService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("businessService")
public class BusinessServiceBean {

	@Inject
	private BusinessService testService;
	
	public void doAuthorizedAction() {
		testService.doAuthorizedAction();
	}

}
