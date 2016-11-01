
var deviceMessage;
var selectedDeviceId;
var initialTimeSeriesData;
var lastDeviceEvent;

function devicesMessageHandler(message) {
	//console.log('devicesMessageHandler: ' + message.data);
	deviceMessage = message;
	if (deviceMessage.deviceListResponse) {
		console.log('device response, rendering ...');
		renderDevicesTable(deviceMessage.deviceListResponse.devices);
	} else if (deviceMessage.deviceListChanged) {
		console.log('device list changed, sending reload request ...');
		webSocketSendMessage({ deviceListRequest: {} });
	} else if (deviceMessage.getStatusResponse) {
		console.log('device status response ...');
		renderDeviceInfo(deviceMessage.getStatusResponse);
	} else if (deviceMessage.deviceEvent) {
		console.log('device process device event ...');
		processDeviceEvent(deviceMessage.deviceEvent);
	} else if (deviceMessage.timeSeriesDataResponse) {
		console.log('device process timeseries response ...');
		initialTimeSeriesData = deviceMessage.timeSeriesDataResponse;
		showInitialChartData(initialTimeSeriesData);
	} else {
		console.log('unsupported message: ' + deviceMessage);
	}
}

function initDevicesUI() {
	console.log('initDevicesUI');
	messageHandler = devicesMessageHandler;
	webSocketTopicSubscribe('/devices');
	setElementVisible("#devicesInfoPanel", true);
	webSocketSendMessage({ deviceListRequest: {} });
}

function shutdownDevicesUI() {
	console.log('shutdownDevicesUI');
	webSocketTopicUnsubscribe('/devices');
	if (selectedDeviceId != undefined) {
		webSocketTopicUnsubscribe('/devices/' + selectedDeviceId);
	}
	messageHandler = undefined;
	setElementVisible("#devicesInfoPanel", false);
	setElementVisible("#deviceInfoPanel", false);
	$('#devicesTableBody').empty();
} 

function renderDevicesTable(data) {
	console.log('renderDevicesTable');
	$('#devicesTableBody').empty();
	if (data === undefined) return;
	for (i = 0; i < data.length; i++) { 
		row = document.createElement("tr");
		first = document.createElement("td");
		first.innerHTML = data[i].deviceId;
		row.appendChild(first);
		second = document.createElement("td");
		second.innerHTML = data[i].sessionId;
		row.appendChild(second);
		third = document.createElement("td");
		third.innerHTML = data[i].connected;
		row.appendChild(third);
		row.onclick = (function() { var deviceId = data[i].deviceId; return function() {showSelectedDevice(deviceId);} })();
		$('#devicesTableBody').append(row);
	}
}

function showSelectedDevice(deviceId) {
	console.log('onDeviceSelected: ' + deviceId);
	selectedDeviceId = deviceId;
	setElementVisible("#devicesInfoPanel", false);
	setElementVisible("#deviceInfoPanel", true);
	webSocketSendMessage({ getStatusRequest: { deviceId: deviceId } });
	initTimeChart();
	getTimeSeriesData();
}

function showDevicesList() {
	console.log('showDevicesList: ');
	setElementVisible("#devicesInfoPanel", true);
	setElementVisible("#deviceInfoPanel", false);
	if (selectedDeviceId) {
		webSocketTopicUnsubscribe('/devices/' + selectedDeviceId);
		cleanTimeChart();
	}
}

function renderDeviceInfo(data) {
	console.log('renderDeviceInfo');
	if (data === undefined) {
	   $('#deviceInfoHeader').empty();
	   $('#temperatureValue').empty();
	   $('#relativeHumidityValue').empty();
	   $('#pressureValue').empty();
	   setControlValue('#control0Value', false);
	   setControlValue('#control1Value', false);
	} else {
	   $('#deviceInfoHeader').text(data.deviceId);
	   $('#temperatureValue').text(data.temperature);
	   $('#relativeHumidityValue').text(data.relativeHumidity);
	   $('#pressureValue').text(data.pressure);
	   setControlValue('#control0Value', data.controlOutputs[0]);
	   setControlValue('#control1Value', data.controlOutputs[1]);
	}
    setControlValue('#button0Value', false);
	setControlValue('#button1Value', false);
	webSocketTopicSubscribe('/devices/' + selectedDeviceId);
}

function setControlValue(elementIdSelector, value) {
	if (value) {
		$(elementIdSelector).removeClass('label-danger').addClass('label-success');
		$(elementIdSelector).text('on');
	} else {
		$(elementIdSelector).removeClass('label-success').addClass('label-danger');
		$(elementIdSelector).text('off');
	}
}

function toggleControl(index) {
	if ($('#control' + index + 'Value').text() === 'on') {
		console.log('toggleControl ' + selectedDeviceId + ' [' + index + ']:off');
		webSocketSendMessage({ setControlOutputRequest: { deviceId: selectedDeviceId, pinId: index, state: false } });
	} else {
		console.log('toggleControl ' + selectedDeviceId + ' [' + index + ']:on');
		webSocketSendMessage({ setControlOutputRequest: { deviceId: selectedDeviceId, pinId: index, state: true } });
	}
}

function processDeviceEvent(deviceEvent) {
	lastDeviceEvent = deviceEvent;
	console.log('processDeviceEvent');
	if (deviceEvent.controlOutputEvent) {
		index = deviceEvent.controlOutputEvent.pinId;
		state = deviceEvent.controlOutputEvent.state;
		setControlValue('#control' + index + 'Value', state);
	} else if (deviceEvent.buttonEvent) {
		index = deviceEvent.buttonEvent.buttonId;
		state = deviceEvent.buttonEvent.state;
		setControlValue('#button' + index + 'Value', state);
	} else if (deviceEvent.sensorEvent) {
        $('#temperatureValue').text(deviceEvent.sensorEvent.temperature);
		$('#relativeHumidityValue').text(deviceEvent.sensorEvent.relativeHumidity);
		$('#pressureValue').text(deviceEvent.sensorEvent.pressure);
		appendChartData(deviceEvent);
	} else {
		console.log('unsupported device event');
	}
}

function getDeviceInfo() {
	webSocketSendMessage({ getStatusRequest: { deviceId: selectedDeviceId } });
}

function getSensorData() {
	webSocketSendMessage({ sensorDataRequest: { deviceId: selectedDeviceId } });
}

function getTimeSeriesData() {
	webSocketSendMessage({ timeSeriesDataRequest: { deviceId: selectedDeviceId } });
}
