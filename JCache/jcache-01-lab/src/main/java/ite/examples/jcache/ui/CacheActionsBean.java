package ite.examples.jcache.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.examples.jcache.services.CacheAccessBean;

@SessionScoped
@Named("cacheActions")
public class CacheActionsBean implements Serializable {

	private static final Logger logger = Logger.getLogger(CacheActionsBean.class.getName());
	private String inKey;
	private String inData;

	private String outKey;
	private String outData;

	@Inject
	private CacheAccessBean cacheAccess;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	public String getInData() {
		return inData;
	}

	public void setInData(String inData) {
		this.inData = inData;
	}
	
	public String getInKey() {
		return inKey;
	}

	public void setInKey(String inKey) {
		this.inKey = inKey;
	}

	public String getOutKey() {
		return outKey;
	}

	public void setOutKey(String outKey) {
		this.outKey = outKey;
	}

	public String getOutData() {
		return outData;
	}

	public void storeInCacheAction() {
		logger.info("storeInCacheAction " + inKey + "=" + inData);
		cacheAccess.storeInCache(inKey, inData);
	}

	public void getFromCacheAction() {
		outData = cacheAccess.getFromCache(outKey);
		logger.info("getFromCacheAction " + outKey + "=" + outData);
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
