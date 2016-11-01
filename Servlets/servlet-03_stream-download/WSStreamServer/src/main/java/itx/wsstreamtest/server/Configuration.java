package itx.wsstreamtest.server;

import javax.enterprise.context.Dependent;

@Dependent
public class Configuration {

	private final static String DATA_SOURCE_PATH = "/home/gergej/Data/AS_instances/binaryblob.8g"; 
	
	public String getDataSourcePath() {
		return DATA_SOURCE_PATH;
	}

}
