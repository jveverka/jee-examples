package itx.asynctest.server;

public class Report {
	
	private long started;
	private long duration;
	private String name;
	private String result;
	private String startedBy;
	private String triggeredBy;
	
	public Report() {
	}
	
	public Report(long started, long duration, String name, String result, String startedBy, String triggeredBy) {
		super();
		this.started = started;
		this.duration = duration;
		this.name = name;
		this.result = result;
		this.startedBy = startedBy;
		this.triggeredBy = triggeredBy;
	}

	public long getStarted() {
		return started;
	}
	
	public void setStarted(long started) {
		this.started = started;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	public String getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}
	
	public String getTriggeredBy() {
		return triggeredBy;
	}

	public void setTriggeredBy(String triggeredBy) {
		this.triggeredBy = triggeredBy;
	}

	@Override
	public String toString() {
		return "Report:" + started + ":" + duration + ":" + name + ":" + startedBy + ":" + triggeredBy + ":" + result;
	}
	
}
