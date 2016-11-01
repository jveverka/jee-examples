package ite.examples.mdb.utils;

import ite.examples.mdb.MessageData;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import java.util.logging.Logger;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Singleton
@Startup
public class BlackBoxBean {
	
	private static final Logger logger = Logger.getLogger(BlackBoxBean.class.getName());
	private List<MessageData> messages;

	@PostConstruct
	private void init() {
		logger.info("init ...");
		messages = new ArrayList<>();
	}

	public void addMessageFromTopic(String message) {
		logger.info("BlackBox:addMessage: " + message);
		messages.add(new MessageData(new Date(), "Topic", message));
	}

	public void addMessageFromQueue(String message) {
		logger.info("BlackBox:addMessage: " + message);
		messages.add(new MessageData(new Date(), "Queue", message));
	}

	public List<MessageData> getMessages() {
		return new ArrayList<MessageData>(messages);
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
