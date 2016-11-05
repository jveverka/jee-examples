package itx.examples.swagger.ws.dto;

public class ApplicationInfo {
	
	private String name;
	private String version;
	private long currentTime;
	
	public ApplicationInfo() {
	}
	
	public ApplicationInfo(String name, String version, long currentTime) {
		super();
		this.name = name;
		this.version = version;
		this.currentTime = currentTime;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}
	
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

}
