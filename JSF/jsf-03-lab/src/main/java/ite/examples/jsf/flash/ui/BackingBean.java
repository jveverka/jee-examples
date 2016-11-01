package ite.examples.jsf.flash.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@SessionScoped
@Named("data")
public class BackingBean implements Serializable {

	private static final Logger logger = Logger.getLogger(BackingBean.class.getName());
	private String data;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
	}

	public String getData() {
		return data;
	}

	/*public void setData(String data) {
		this.data = data;
	}*/
	
	public boolean isFlashDataValid() {
		return data != null;
	}
	
	public void loadFlashData() {
		logger.info("loadFlashData");
		Object dataObj = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("data");
		if (dataObj != null) {
			data = dataObj.toString();
		} else {
			data = null;
		}
	}
	
	public String doSubmitAction() {
		logger.info("doSubmitAction");
		return "next";
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
