package ite.examples.services.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import ite.examples.services.SimpleRowHolder;
import ite.examples.services.Utils;

@Singleton
@Startup
@ApplicationScoped
public class DataCacheService {
	
	private static final Logger logger = Logger.getLogger(DataCacheService.class.getName());
	
	private List<SimpleRowHolder> cleanData;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		cleanData = new ArrayList<>();
		cleanData.add(new SimpleRowHolder(new String[] { "1a","1b","1c","1d" }));
		cleanData.add(new SimpleRowHolder(new String[] { "2a","2b","2c","2d" }));
		cleanData.add(new SimpleRowHolder(new String[] { "3a","3b","3c","3d" }));
		cleanData.add(new SimpleRowHolder(new String[] { "4a","4b","4c","4d" }));
	}
	
	public List<SimpleRowHolder> getDataCopy() {
		return Utils.getDataCopy(cleanData);
	}
	
	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
