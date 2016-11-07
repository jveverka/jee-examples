package itx.examples.jcache.services;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

@Stateless
public class CacheAccessBean {

	private static final Logger logger = Logger.getLogger(CacheAccessBean.class.getName());

	@Resource(lookup="java:jboss/infinispan/container/jcacheTest")
    private CacheContainer container;
	
	private Cache<String, String> cache;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		cache = container.getCache();
	}
	
	public void storeInCache(String key, String value) {
		cache.put(key, value);
		if ("null".equals(key) || key == null || key.length()==0) {
			throw new NullPointerException();
		}
	}

	public String getFromCache(String key) {
		return cache.get(key);
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
