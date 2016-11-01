package ite.examples.jsf.realtime.services;

import ite.examples.jsf.realtime.services.push.EventDispatcher;
import ite.examples.jsf.realtime.services.push.EventMessage;

import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class DataGeneratorService {
	
	private static final Logger logger = Logger.getLogger(DataGeneratorService.class.getName());
	private static final int MAX_CHART_BUFFER = 128;
	private static final int MAX = 100;
	private static final int TIMEOUT = 3;
	private int[] chartDataX;
	private int[] chartDataY;
	private int lastRandom;
	private int counter;
	private int maxcounter;
	private Random random;
	
	@Inject
	private EventDispatcher eventDispatcher;
	
	@PostConstruct
	private void init() {
		logger.info("init ...");
		chartDataX = new int[MAX_CHART_BUFFER];
		chartDataY = new int[MAX_CHART_BUFFER];
		random = new Random();
		for (int i=0; i<MAX_CHART_BUFFER; i++) {
			lastRandom = random.nextInt(100);
			chartDataX[i] = i;
			chartDataY[i] = lastRandom;
		}
		counter = MAX_CHART_BUFFER - 1;
		maxcounter = 0;
	}
	
	public int getLiveDataTimeout() {
		return TIMEOUT;
	}
	
	public DataSet getFullDataSet() {
		DataSet ds = new DataSet();
		ds.setCounter(counter);
		ds.setLastRandom(lastRandom);
		ds.setMaxcounter(maxcounter);
		int[][] chartData = new int[MAX_CHART_BUFFER][2];
		for (int i=0; i<MAX_CHART_BUFFER; i++) {
			chartData[i][0] = chartDataX[i];
			chartData[i][1] = chartDataY[i];
		}
		ds.setChartData(chartData);
		ds.setType(DataSetType.FULL);
		return ds;
	}

	@Schedule(second = "*/" + TIMEOUT, minute="*", hour="*", persistent=false)
	public void createDataEvent() {
		int randomNumber = random.nextInt(100);
		counter++;
		for (int i=1; i<MAX_CHART_BUFFER; i++) {
			chartDataX[i-1] = chartDataX[i];
			chartDataY[i-1] = chartDataY[i];
		}
		chartDataX[MAX_CHART_BUFFER - 1] = counter;
		chartDataY[MAX_CHART_BUFFER - 1] = randomNumber;
		lastRandom = randomNumber;
		maxcounter++;
		if (maxcounter > MAX) {
			maxcounter = 0;
		}
		DataSet ds = new DataSet();
		ds.setCounter(counter);
		ds.setLastRandom(lastRandom);
		ds.setMaxcounter(maxcounter);
		ds.setChartData(new int[][] { { chartDataX[MAX_CHART_BUFFER - 1], chartDataY[MAX_CHART_BUFFER - 1] } });
		ds.setType(DataSetType.UPDATE);
		EventMessage em = new EventMessage("DataSetEvent", ds); 
		eventDispatcher.fireMessageDataEvent(em);
		//logger.info("data event: random=" + randomNumber + " counter=" + counter + " maxcounter=" + maxcounter);
	}

	@PreDestroy
	private void deinit() {
		logger.info("deinit ...");
	}
	
}
