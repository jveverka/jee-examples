
var adminMessage;

function initAdminUI() {
	console.log("initAdminUI");
	messageHandler = adminMessageHandler;
	webSocketTopicSubscribe('/userinfo');
}

function shutdownAdminUI() {
	console.log("shutdownAdminUI");
	messageHandler = undefined;
	webSocketTopicUnsubscribe('/userinfo');
	$('#adminDataHolder').empty();
}

function adminMessageHandler(message) {
	adminMessage = message;
	if (adminMessage.userInfoData) {
		console.log('user info rendering: ' + message.data);
		showUserInfoData(adminMessage.userInfoData);
	} else {
		console.log('adminMessageHandler: unsupported message: ' + deviceMessage);
	}
}

function showUserInfoData(userInfoData) {
	console.log('showUserInfoData');
	$('#adminDataHolder').empty();
	
	header = document.createElement('h3');
	header.innerHTML = 'HTTP Sessions';
	table = document.createElement('table');
	table.classList.add("table");
	table.classList.add("table-hover");
	tbody = document.createElement('tbody');
	for (var i=0; i<userInfoData.httpSessions.length; i++) {
		row = document.createElement('tr');
		td = document.createElement('td');
		td.innerHTML = userInfoData.httpSessions[i].httpSessionId; 
		row.appendChild(td);
		td = document.createElement('td');
		td.innerHTML = userInfoData.httpSessions[i].userName; 
		row.appendChild(td);
		td = document.createElement('td');
		td.innerHTML = userInfoData.httpSessions[i].protocol; 
		row.appendChild(td);
		tbody.appendChild(row);
	}
	table.appendChild(tbody);
	$('#adminDataHolder').append(header);
	$('#adminDataHolder').append(table);
	
	header = document.createElement('h3');
	header.innerHTML = 'WebSocket Sessions';
	table = document.createElement('table');
	table.classList.add("table");
	table.classList.add("table-hover");
	tbody = document.createElement('tbody');
	for (var i=0; i<userInfoData.wsSessions.length; i++) {
		row = document.createElement('tr');
		td = document.createElement('td');
		td.innerHTML = userInfoData.wsSessions[i].wsSessionId; 
		row.appendChild(td);
		td = document.createElement('td');
		td.innerHTML = userInfoData.wsSessions[i].userName; 
		row.appendChild(td);
		td = document.createElement('td');
		td.innerHTML = userInfoData.wsSessions[i].protocol; 
		row.appendChild(td);
		tbody.appendChild(row);
	}
	table.appendChild(tbody);
	$('#adminDataHolder').append(header);
	$('#adminDataHolder').append(table);

	header = document.createElement('h3');
	header.innerHTML = 'Topics';
	table = document.createElement('table');
	table.classList.add("table");
	table.classList.add("table-hover");
	tbody = document.createElement('tbody');
	for (var i=0; i<userInfoData.topics.length; i++) {
		row = document.createElement('tr');
		td = document.createElement('td');
		td.innerHTML = userInfoData.topics[i].topicId; 
		row.appendChild(td);
		td = document.createElement('td');
		for (var j=0; j<userInfoData.topics[i].wsSessionId.length; j++) {
			span = document.createElement('span');
			span.innerHTML = userInfoData.topics[i].wsSessionId[j];
			td.appendChild(span);
			td.appendChild(document.createElement('br'));
		}
		row.appendChild(td);
		tbody.appendChild(row);
	}
	table.appendChild(tbody);
	$('#adminDataHolder').append(header);
	$('#adminDataHolder').append(table);

	header = document.createElement('h3');
	header.innerHTML = 'HttpSession having WebSocket sessions';
	table = document.createElement('table');
	table.classList.add("table");
	table.classList.add("table-hover");
	tbody = document.createElement('tbody');
	for (var i=0; i<userInfoData.httpSessionWsSessions.length; i++) {
		row = document.createElement('tr');
		td = document.createElement('td');
		td.innerHTML = userInfoData.httpSessionWsSessions[i].httpSessionId; 
		row.appendChild(td);
		td = document.createElement('td');
		for (var j=0; j<userInfoData.httpSessionWsSessions[i].wsSessionId.length; j++) {
			span = document.createElement('span');
			span.innerHTML = userInfoData.httpSessionWsSessions[i].wsSessionId[j];
			td.appendChild(span);
			td.appendChild(document.createElement('br'));
		}
		row.appendChild(td);
		tbody.appendChild(row);
	}
	table.appendChild(tbody);
	$('#adminDataHolder').append(header);
	$('#adminDataHolder').append(table);

}