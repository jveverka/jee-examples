package ite.examples.jsf.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlOutputLabel;

@FacesComponent(value = OutputHeader.COMPONENT_TYPE, createTag = true)
public class OutputHeader extends HtmlOutputLabel {

	private static final String DEFAULT_RENDERER = "ite.examples.jsf.component.OutputHeaderRenderer";
	private static final String STYLE_CLASS_KEY = "styleClass";

	public static final String COMPONENT_TYPE = "ite.examples.jsf.component.OutputHeaderk";
	public static final String COMPONENT_FAMILY = "ite.examples.jsf.component";
	public final static String DEFAULT_STYLE_CLASS = "itx-outputheader";

	public OutputHeader() {
		setRendererType(DEFAULT_RENDERER);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

    public String getStyleClass() {
		return getStateHelper().eval(STYLE_CLASS_KEY, DEFAULT_STYLE_CLASS).toString();
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(STYLE_CLASS_KEY, styleClass);
	}

    public String getStyleClassString() {
    	String styleClass = getStateHelper().eval(STYLE_CLASS_KEY).toString();
    	if (styleClass != null && styleClass.length() > 0) {
    		return DEFAULT_STYLE_CLASS + " " + styleClass;
    	}
		return DEFAULT_STYLE_CLASS;
	}

}
