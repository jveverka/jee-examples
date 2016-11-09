package itx.hybridapp.javafx.loginform;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class ProtocolSelectorHandler implements EventHandler<ActionEvent> {
	
	private static final Logger logger = Logger.getLogger(ProtocolSelectorHandler.class.getName());
	
	private LoginController loginController;
	
	public ProtocolSelectorHandler(LoginController loginController) {
		this.loginController = loginController;
	}

	@Override
	public void handle(ActionEvent event) {
		ComboBox<String> protocolSelector = (ComboBox<String>)event.getSource();
		String mediaType = protocolSelector.getValue();
		logger.info("handle protocol selection: " + mediaType);
		loginController.setSelectedProtocol(mediaType);
	}

}
