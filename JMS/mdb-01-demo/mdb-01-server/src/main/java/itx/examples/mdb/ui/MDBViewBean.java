package itx.examples.mdb.ui;

import itx.examples.mdb.MDBClientBean;
import itx.examples.mdb.MessageData;
import itx.examples.mdb.utils.BlackBoxBean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("mdbview")
public class MDBViewBean implements Serializable {
	
	private String message;
	private List<MessageData> messages;
	
	@Inject
	private BlackBoxBean blackBox;
	
	@Inject
	private MDBClientBean mdbClient;
	
	@PostConstruct
	private void init() {
		realodDataAction();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<MessageData> getMessages() {
		return messages;
	}

	public void sendTopicMessageAction() {
		mdbClient.sendTopicMessageAction(message);
		message = null;
		realodDataAction();
	}
	
	public void sendQueueMessageAction() {
		mdbClient.sendQueueMessageAction(message);
		message = null;
		realodDataAction();
	}
	
	public void realodDataAction() {
		messages = blackBox.getMessages();
	}

}
