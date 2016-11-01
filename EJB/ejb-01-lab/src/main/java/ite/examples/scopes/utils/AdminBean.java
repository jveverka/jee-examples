package ite.examples.scopes.utils;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import java.util.logging.Logger;

@Singleton
@Startup
@Named("admin")
@ApplicationScoped
public class AdminBean {
	
	private static final Logger logger = Logger.getLogger(AdminBean.class.getName());
	private AtomicInteger singletonScopedCounter;
	private AtomicInteger applicationScopedCounter;
	private AtomicInteger sessionScopedCounter;
	private AtomicInteger viewScopedCounter;
	private AtomicInteger requestScopedCounter;
	private AtomicInteger conversationScopedCounter;

	@PostConstruct
	private void init() {
		logger.info("init ...");
		singletonScopedCounter = new AtomicInteger(0);
		applicationScopedCounter = new AtomicInteger(0);
		sessionScopedCounter = new AtomicInteger(0);
		viewScopedCounter = new AtomicInteger(0);
		requestScopedCounter = new AtomicInteger(0);
		conversationScopedCounter = new AtomicInteger(0);
	}
	
	public void registerSingleton() {
		singletonScopedCounter.incrementAndGet();
	}
	
	public int getSingletonCount() {
		return singletonScopedCounter.get();
	}

	public void unregisterSingleton() {
		singletonScopedCounter.decrementAndGet();
	}

	public void registerAppScoped() {
		applicationScopedCounter.incrementAndGet();
	}
	
	public int getAppScopedCount() {
		return applicationScopedCounter.get();
	}

	public void unregisterAppScoped() {
		applicationScopedCounter.decrementAndGet();
	}

	public void registerSessionScoped() {
		sessionScopedCounter.incrementAndGet();
	}
	
	public int getSessionScopedCount() {
		return sessionScopedCounter.get();
	}

	public void unregisterSessionScoped() {
		sessionScopedCounter.decrementAndGet();
	}
	
	public void registerViewScoped() {
		viewScopedCounter.incrementAndGet();
	}
	
	public int getViewScopedCount() {
		return viewScopedCounter.get();
	}

	public void unregisterViewScoped() {
		viewScopedCounter.decrementAndGet();
	}

	public void registerRequestScoped() {
		requestScopedCounter.incrementAndGet();
	}
	
	public int getRequestScopedCount() {
		return requestScopedCounter.get();
	}

	public void unregisterRequestScoped() {
		requestScopedCounter.decrementAndGet();
	}

	public void registerConversationScoped() {
		conversationScopedCounter.incrementAndGet();
	}
	
	public int getConversationScopedCount() {
		return conversationScopedCounter.get();
	}

	public void unregisterConversationScoped() {
		conversationScopedCounter.decrementAndGet();
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
