
var dataSize = 0;
var dataSeries;
var chartSetup;
var plotChart;
var radialProgress;

function onPageLoad() {
	dataSize = initialDataSet.dataSet.chartData.length;
	chartSetup = { yaxis: {show: true, min: 0, max: 100}, xaxis: {show: true, min: 0, max: dataSize} };
	chartData = initialDataSet.dataSet.chartData;
	dataSeries = [ { label: "Random %", data: chartData, lines: { show: true }, shadowSize: 0 } ];
	plotChart = $.plot("#placeholder", dataSeries , chartSetup );
	plotChart.draw();
	radialProgress = radialProgress(document.getElementById('radialwrapper'))
	   .label("RP")
	   .diameter(330)
	   .minValue(0)
	   .maxValue(100)
	   .unit("%")
	   .value(initialDataSet.dataSet.lastRandom)
	   .render();       
}

function appendChartData(newValue) {
	   var chartData = dataSeries[0].data;
	   for (var i = 1; i < dataSize; ++i) {
	      chartData[i-1][0] = i -1;
	      chartData[i-1][1] = chartData[i][1];
	   }
	   chartData[dataSize - 1][0] = dataSize - 1;
	   chartData[dataSize - 1][1] = newValue;
	   dataSeries[0].data = chartData;
	   plotChart.setData(dataSeries);
	   plotChart.draw();
}

function updateProgress(newValue) {
	radialProgress.value(newValue).render(); 
}


function handlePushGlobalEvent(eventData) {
	console.log("PFPush GlobalEvent: " + eventData.eventType);
	appendChartData(eventData.dataSet.chartData[0][1]);
	updateProgress(eventData.dataSet.chartData[0][1]);
}
