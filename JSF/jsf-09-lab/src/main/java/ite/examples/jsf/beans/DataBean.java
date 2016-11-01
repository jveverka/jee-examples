package ite.examples.jsf.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("dataBean")
@SessionScoped
public class DataBean implements Serializable {
	
	private String text1;
	private String text2;
	
	@PostConstruct
	private void init() {
		text1 = "Header1";
		text2 = "Header2";
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

}
