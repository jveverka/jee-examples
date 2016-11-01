package ite.examples.scopes.cdi;

import ite.examples.scopes.utils.AdminBean;
import ite.examples.scopes.utils.IdGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ConversationScoped
@Named("cscoped")
public class ConversationScopedBean implements Serializable {
	
	private static final Logger logger = Logger.getLogger(ConversationScopedBean.class.getName());
	private int myId;
	private String name;
	private List<String> messages;
	private int index;
	
	@Inject
	private Conversation conversation;

	@Inject
	private AdminBean admin;

	@PostConstruct
	private void init() {
		myId = IdGenerator.getNextId();
		name = "RequestScopedBean";
		logger.info("init [" + myId + "] ...");
		admin.registerConversationScoped();
		messages = new ArrayList<>();
		messages.add("");
		messages.add("Step1");
		messages.add("Step2");
		messages.add("Step3");
		messages.add("Step4");
		index = 0;
	}
	
	public String startConversation() {
		logger.info("start conversation [" + myId + "] ...");
		conversation.begin();
		index = 1;
		return "";
	}
	
	public String next() {
		if (index < (messages.size() - 1)) {
			index++;
			logger.info("goto next step [" + myId + "] index=" + index);
		} else {
			logger.info("no next step available [" + myId + "]"); 
		}
		return "";
	}
	
	public String getCurrentMessage() {
		return messages.get(index);
	}

	public String stopConversation() {
		logger.info("stop conversation [" + myId + "]");
		conversation.end();
		index = 0;
		return "";
	}
	
	public int getId() {
		return myId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getConversationId() {
		return conversation.getId();
	}
	
	public boolean hasStarted() {
		if (conversation.getId() != null) {
			return true;
		}
		return false;
	}
	
	public boolean hasNextStep() {
		if (index < (messages.size()-1) && hasStarted()) {
			return true;
		}
		return false;
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit [" + myId + "] ...");
		admin.unregisterConversationScoped();
	}
	
}
