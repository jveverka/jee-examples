
var testMessage;
var runningTestsCounter = 0;

function initTestUI() {
	console.log('initTestUI');
	messageHandler = testsMessageHandler;
	webSocketTopicSubscribe('/userinfo');
	webSocketTopicSubscribe('/tests');
	webSocketSendMessage({ testResultListRequest: {} });
}

function shutdownTestUI() {
	console.log('shutdownTestUI');
	messageHandler = undefined;
	webSocketTopicUnsubscribe('/tests');
	webSocketTopicUnsubscribe('/userinfo');
}

function testsMessageHandler(message) {
	//console.log('testMessageHandler: ');
	testMessage = message;
	if (testMessage.userInfoData) {
		showWSSessions(testMessage.userInfoData);
	} else if (testMessage.testResultListResponse) {
		showTestResults(testMessage.testResultListResponse);
	}
}

function onWSTestSelect() {
	console.log('onWSTestSelect');
}

function submitTestJob() {
	selectedWsId = $("#inputTestWsList").val();
	repeat = document.getElementById('inputTestRepeatId').value;
	payload = document.getElementById('inputTestPayloadId').value;
	console.log('submitTestJob: ' + selectedWsId + "/" + payload + "/" + repeat);
	webSocketSendMessage({ testJobRequest: { wsSessionId: selectedWsId, payload: payload, repeat: parseInt(repeat) } });
	runningTestsCounter++;
	setProgress(true);
	$('#runningTestCounterId').text(runningTestsCounter);
}

function showWSSessions(userInfoData) {
	console.log('showWSSessions');
	$('#inputTestWsList').empty();
	for (var i=0; i<userInfoData.wsSessions.length; i++) {
		option = document.createElement('option');
		option.innerHTML = userInfoData.wsSessions[i].userName + ' :: ' + userInfoData.wsSessions[i].wsSessionId;
		option.value = userInfoData.wsSessions[i].wsSessionId;
		$('#inputTestWsList').append(option);
	}
}

function setProgress(animate) {
	if (animate) {
		$('#testProgressBarId').addClass('active');
	} else {
		$('#testProgressBarId').removeClass('active');
	}
}

function showTestResults(testResultListResponse) {
	console.log('showTestResults');
	$('#testResults').empty();
	for (var i=0; i<testResultListResponse.testResults.length; i++) {
		row = document.createElement('tr');
		
		td = document.createElement('td');
		td.innerHTML = testResultListResponse.testResults[i].testId;
		row.appendChild(td);

		td = document.createElement('td');
		td.innerHTML = testResultListResponse.testResults[i].publishDuration;
		row.appendChild(td);

		duration = testResultListResponse.testResults[i].duration;
		td = document.createElement('td');
		td.innerHTML = duration;
		row.appendChild(td);

		td = document.createElement('td');
		td.innerHTML = testResultListResponse.testResults[i].status;
		row.appendChild(td);

		repeat = testResultListResponse.testResults[i].request.repeat;
		td = document.createElement('td');
		td.innerHTML = repeat;
		row.appendChild(td);

		td = document.createElement('td');
		td.innerHTML = testResultListResponse.testResults[i].protocol;
		row.appendChild(td);

		td = document.createElement('td');
		td.innerHTML = testResultListResponse.testResults[i].clientId;
		row.appendChild(td);

		td = document.createElement('td');
		td.innerHTML = (repeat/(duration/1000)).toFixed(2);
		row.appendChild(td);

		$('#testResults').append(row);
	}
	if (runningTestsCounter > 0) runningTestsCounter--;
	if (runningTestsCounter == 0) setProgress(false);
	$('#runningTestCounterId').text(runningTestsCounter);
}
