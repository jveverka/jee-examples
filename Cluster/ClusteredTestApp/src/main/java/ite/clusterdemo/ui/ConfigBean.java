package ite.clusterdemo.ui;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ite.clusterdemo.services.ConfigData;

@ViewScoped
@Named("configBean")
public class ConfigBean implements Serializable {
	
	@Inject
	private ConfigData config;

	public String getHostname() {
		return config.getHostname();
	}

	public String getServerId() {
		return config.getServerId();
	}
	
}
