package ite.examples.ejb.batch.services;

import javax.batch.api.listener.JobListener;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named("CreateReportListener")
public class CreateReportListener implements JobListener {

	@Override
	public void beforeJob() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterJob() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
