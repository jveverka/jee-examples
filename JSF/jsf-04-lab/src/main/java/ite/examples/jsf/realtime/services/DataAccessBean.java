package ite.examples.jsf.realtime.services;

import ite.examples.jsf.realtime.services.push.DataUtils;
import ite.examples.jsf.realtime.services.push.EventDispatcher;
import ite.examples.jsf.realtime.services.push.EventMessage;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Stateless
@Named("dataAccess")
public class DataAccessBean {

	private static final Logger logger = Logger.getLogger(DataAccessBean.class.getName());
	
	@Inject
	private DataGeneratorService dataGenerator;

	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public String getGlobalPushChannelName() {
		return EventDispatcher.PFPUSH_GLOBAL_CHANNEL;
	}
	
	public String getLiveDataTimeout() {
		return dataGenerator.getLiveDataTimeout() + "s";
	}
	
	public String getInitialDataAsJsonString() {
		long start = System.currentTimeMillis();
		DataSet ds = dataGenerator.getFullDataSet();
		EventMessage em = new EventMessage("InitialData", ds);
		String jsonStr = DataUtils.jsonEncode(em);
		logger.info("initial data set loaded in: " + (System.currentTimeMillis() - start) + "ms.");
		return jsonStr;
	}

}
