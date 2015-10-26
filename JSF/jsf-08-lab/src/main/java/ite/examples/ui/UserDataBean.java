package ite.examples.ui;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.examples.services.SimpleRowHolder;
import ite.examples.services.data.UserSessionService;

@ViewScoped
@Named("userData")
public class UserDataBean implements Serializable {

	private static final Logger logger = Logger.getLogger(UserDataBean.class.getName());
	
	private int rowId;
	private int colId;
	private String entry;
	
	@Inject
	private UserSessionService userSession;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}
	
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getColId() {
		return colId;
	}

	public void setColId(int colId) {
		this.colId = colId;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public void updateData() {
		logger.info("updateData: [" + rowId + "," + colId + "] " + entry);
		userSession.updateData(rowId, colId, entry);
	}
	
	public List<SimpleRowHolder> getData() {
		logger.info("getData ...");
		return userSession.getData();
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
