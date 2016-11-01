package itx.hybridapp.rpi;

import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	
	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		logger.info("Started");
		Injector injector = Guice.createInjector(new MainModule());
		Controller controller = injector.getInstance(Controller.class);
		controller.mainLoop();
	}

}
