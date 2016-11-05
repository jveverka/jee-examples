package itx.examples.swagger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.reflections.Reflections;

@ApplicationPath("/ws")
public class RestApplication extends Application {
	
	private static final Logger logger = Logger.getLogger(RestApplication.class.getName());
	
    Set<Class<?>> singletons;
    Set<Object> clazz;

    public RestApplication() {
    	logger.info("RestApplication: init ...");
        Reflections reflections = new Reflections(this.getClass()
                                                      .getPackage()
                                                      .getName());
        singletons = reflections.getTypesAnnotatedWith(javax.ws.rs.Path.class);

        clazz = new HashSet<Object>();

        for (Class<?> annotated_class : singletons) {
            try {
            	logger.info("RestApplication: add class: " + annotated_class.getName());
                clazz.add(annotated_class.newInstance());
            } catch (Exception e) {
            	logger.info("RestApplication: class loading error !");
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<Class<?>> getClasses() {
        return singletons;
    }

    @Override
    public Set<Object> getSingletons() {
        return clazz;
    }

}
