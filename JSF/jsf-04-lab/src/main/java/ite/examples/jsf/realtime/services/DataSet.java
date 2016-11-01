package ite.examples.jsf.realtime.services;

public class DataSet {

	private int lastRandom;
	private int counter;
	private int maxcounter;
	private int[][] chartData;
	private DataSetType type;
	
	public DataSet() {
	}
	
	public DataSet(int lastRandom, int counter, int maxcounter, int[][] chartData, DataSetType type) {
		super();
		this.lastRandom = lastRandom;
		this.counter = counter;
		this.maxcounter = maxcounter;
		this.chartData = chartData;
		this.type = type;
	}

	public int getLastRandom() {
		return lastRandom;
	}

	public void setLastRandom(int lastRandom) {
		this.lastRandom = lastRandom;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getMaxcounter() {
		return maxcounter;
	}

	public void setMaxcounter(int maxcounter) {
		this.maxcounter = maxcounter;
	}

	public int[][] getChartData() {
		return chartData;
	}

	public void setChartData(int[][] chartData) {
		this.chartData = chartData;
	}

	public DataSetType getType() {
		return type;
	}

	public void setType(DataSetType type) {
		this.type = type;
	}
	
}
