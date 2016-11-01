package ite.examples.template.ui;

import ite.examples.data.entities.Employee;
import ite.examples.template.services.DataAccessService;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("dataAction")
public class DataActionBean implements Serializable {

	private static final Logger logger = Logger.getLogger(DataActionBean.class.getName());
	
	@Inject
	private DataAccessService das;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public void startAction() {
		Long empId = das.createEmployeeAction();
		Employee employee = das.getEmployeeById(empId);
		logger.info("employee.firstName:" + employee.getFirstName());
		logger.info("employee.secondName:" + employee.getSecondName());
		logger.info("employee.address.city:" + employee.getAddress().getCity());
		logger.info("employee.jobRecords[0].jobTitle:" + employee.getJobRecords().get(0).getJobTitle());
		logger.info("employee.jobRecords[0].started:" + employee.getJobRecords().get(0).getStarted());
		//logger.info("employee.projects[0].name" + employee.getProjects().get(0).getName());
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
