package itx.hybridapp.javafx;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class AppCloseHandler implements EventHandler<WindowEvent> {
	
	private static final Logger logger = Logger.getLogger(AppCloseHandler.class.getName());

	@Override
	public void handle(WindowEvent event) {
		logger.info("main window closed, performing automatic logout ...");
		try {
			Main.getInstance().doLogout();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

}
