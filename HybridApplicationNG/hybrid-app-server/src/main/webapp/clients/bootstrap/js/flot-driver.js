
var series = 2; 
var seriesColors = [ "red",  "orange" ]; 
var seriesLabels = [ "Temperature", "Humidity" ];  
var dataSeries;
var chartSetupInitial = { yaxis: {show: true, min: 0, max: 0 }, xaxis: { mode: "time", show: true, min: 0, max: 0 } };
var chartSetup;

var plot;

function initTimeChart() {
   console.log("initTimeChart");
   chartSetup = JSON.parse(JSON.stringify(chartSetupInitial));
   /*
   $("#deviceTimeChart").bind("plothover", function (event, pos, item) {
	   console.log("plothover");	
	   var x = item.datapoint[0].toFixed(2);
	   var y = item.datapoint[1].toFixed(2);
	   if (item) {
		   $("#tooltip").text(x + ":" + y).css({top: item.pageY+5, left: item.pageX+5}).fadeIn(200);
	   } else {
		   $("#tooltip").hide();
	   }
   });
   */
   dataSeries = [];
   for (var s = 0; s < series; s++) {
      chartData = [];
      dataSeries[s] = { 
    		  label: seriesLabels[s], 
    		  data: chartData, 
    		  lines: { lineWidth:2, fill:0.4, show: true }, 
    		  points: { show: true },
    		  grid: { hoverable: true},
    		  shadowSize: 0, color: seriesColors[s] 
      };
   }
   dataSeries = dataSeries.reverse();
   plot = $.plot("#deviceTimeChart", dataSeries , chartSetup );
   plot.draw();
}

function showInitialChartData(data) {
   console.log("showInitialChartData");	
   reversedData = data.timeDataHolder.reverse();
   for (var i = 0; i<data.timeDataHolder.length; i++) {
	   dataSeries[0].data[i] = [ reversedData[i].timeStamp, reversedData[i].relativeHumidity ];
	   dataSeries[1].data[i] = [ reversedData[i].timeStamp, reversedData[i].temperature ];
	   setMinYMax(reversedData[i].relativeHumidity);
	   setMinYMax(reversedData[i].temperature);
   }
   chartSetup.xaxis.min = data.timeDataHolder[0].timeStamp;
   chartSetup.xaxis.max = data.timeDataHolder[data.timeDataHolder.length-1].timeStamp;
   plot = $.plot("#deviceTimeChart", dataSeries , chartSetup );
   plot.draw();
}

function appendChartData(deviceEvent) {
    console.log("appendChartData");	
	dataSeries[0].data.shift();
	dataSeries[0].data[dataSeries[0].data.length] = [ deviceEvent.sensorEvent.timeStamp, deviceEvent.sensorEvent.relativeHumidity ];
	dataSeries[1].data.shift();
	dataSeries[1].data[dataSeries[1].data.length] = [ deviceEvent.sensorEvent.timeStamp, deviceEvent.sensorEvent.temperature ];
    chartSetup.xaxis.min = dataSeries[0].data[0][0];
	chartSetup.xaxis.max = deviceEvent.sensorEvent.timeStamp;
    setMinYMax(deviceEvent.sensorEvent.relativeHumidity);
	setMinYMax(deviceEvent.sensorEvent.temperature);
	plot = $.plot("#deviceTimeChart", dataSeries , chartSetup );
	plot.draw();
}

function setMinYMax(number) {
	if ((number + 2) > chartSetup.yaxis.max) chartSetup.yaxis.max = (number + 2);
	if ((number - 2) < chartSetup.yaxis.min) chartSetup.yaxis.min = (number - 2);
	//console.log("MINMAX: " + chartSetup.yaxis.min + " " + chartSetup.yaxis.max);
}

function cleanTimeChart() {
	console.log("cleanTimeChart");	
    chartSetup = JSON.parse(JSON.stringify(chartSetupInitial));
	dataSeries = [];
    plot = $.plot("#deviceTimeChart", dataSeries , chartSetup );
	plot.draw();
}
		