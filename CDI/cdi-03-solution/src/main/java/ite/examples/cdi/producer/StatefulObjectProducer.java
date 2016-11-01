package ite.examples.cdi.producer;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

@Dependent
public class StatefulObjectProducer {

	@Produces 
	private StateHolder createStatefulObject() {
		StateHolder sh = new StateHolder("justCreated");
		return sh;
	}
	
	private void destroyStatefulObject(@Disposes StateHolder sh) {
		sh.setState("closeStatefulObject");
	}
	
}
