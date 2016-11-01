package ite.examples.jsf.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@FacesRenderer(componentFamily = OutputHeader.COMPONENT_FAMILY, rendererType = OutputHeaderRenderer.TYPE)
public class OutputHeaderRenderer extends Renderer {
	
	public static final String TYPE = "ite.examples.jsf.component.OutputHeaderRenderer";
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent uicomponent) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		OutputHeader component = (OutputHeader) uicomponent;
		writer.startElement("h1", component);
		writer.writeAttribute("id", uicomponent.getClientId(), "id");
		writer.writeAttribute("class", component.getStyleClassString(), "class");
		writer.write(component.getValue().toString());
		writer.endElement("h1");
	}

}
