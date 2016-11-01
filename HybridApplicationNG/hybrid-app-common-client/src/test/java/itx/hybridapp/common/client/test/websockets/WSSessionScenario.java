package itx.hybridapp.common.client.test.websockets;

import java.util.List;
import java.util.logging.Logger;


public class WSSessionScenario {
	
	private boolean useBinary;
	private List<ScenarioStep> steps;
	
	public WSSessionScenario(List<ScenarioStep> steps, boolean useBinary) {
		this.useBinary = useBinary;
		this.steps = steps;
	}
	
	public List<ScenarioStep> getSteps() {
		return steps;
	}

	public boolean isUseBinary() {
		return useBinary;
	}

}
