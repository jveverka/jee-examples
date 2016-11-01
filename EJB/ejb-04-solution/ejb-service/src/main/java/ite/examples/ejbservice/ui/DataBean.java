package ite.examples.ejbservice.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.examples.ejbservice.services.DataServiceLocal;


@SessionScoped
@Named("dataService")
public class DataBean implements Serializable {

	private static final Logger logger = Logger.getLogger(DataBean.class.getName());
	private String data;
	private String result;
	
	@Inject
	private DataServiceLocal ds;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		data = "";
		result = "";
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void doSubmitAction() {
		logger.info("doSubmitAction");
		result = ds.getDataLocal(data);
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}

}
