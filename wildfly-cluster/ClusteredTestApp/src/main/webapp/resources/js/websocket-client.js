var wsUri = getRootUri() + "/ClusteredTestApp/wsendpoint";
var id = 0;
var websocket;
var mySessionIdValue;
var myServerIdValue;
var myHostNameValue;
var sessionList;

function getRootUri() {
	return (document.location.protocol == "http:" ? "ws://" : " wss://")
			+ (document.location.hostname == "" ? "localhost"
					: document.location.hostname) + ":"
			+ document.location.port;
}

function connect() {
	websocket = new WebSocket(wsUri);
	websocket.onopen = function(evt) {
		onOpen(evt)
	};
	websocket.onmessage = function(evt) {
		onMessage(evt)
	};
	websocket.onerror = function(evt) {
		onError(evt)
	};
	websocket.onclose = function(evt) {
		onClose(evt)
	};
}

function sendMessage() {
	if (!validateInputForm()) {
		return;
	}
	id = id + 1;
	userId = document.getElementById("userIdInputId").value;
	sessionId = document.getElementById("sessionIdInputId").value;
	messageText = document.getElementById("messageInputId").value;
	timestamp = new Date().getTime();
	payloadJsonString = JSON.stringify({
		message : messageText,
		userId: userId,
		serverId : myServerIdValue,
		hostname : myHostNameValue
	});
	message = {
		sourceId : mySessionIdValue,
		targetId : sessionId,
		messageId : id.toString(),
		contextId : "",
		messageType : "dataMessage",
		returnCode : "OK",
		payloadData : payloadJsonString
	}
	doSend(message);
}

function disconnect() {
	websocket.close();
	connectBtn = document.getElementById("btnConnect");
	connectBtn.disabled = false;
	disconnectBtn = document.getElementById("btnDisconnect");
	disconnectBtn.disabled = true;
	sendBtn = document.getElementById("btnSend");
	sendBtn.disabled = true;
}

function onOpen(evt) {
	clearScreen();
	writeToScreen({messageType: "localws", message: "Connected to Endpoint!"});
	connectBtn = document.getElementById("btnConnect");
	connectBtn.disabled = true;
	disconnectBtn = document.getElementById("btnDisconnect");
	disconnectBtn.disabled = false;
	sendBtn = document.getElementById("btnSend");
	sendBtn.disabled = false;

	id = id + 1;
	message = {
		sourceId : "",
		targetId : "server",
		messageId : id.toString(),
		contextId : "",
		messageType : "getLocalServerInfo",
		returnCode : "",
		payloadData : ""
	}
	doSend(message);

}

function onMessage(evt) {
	messageData = JSON.parse(evt.data);
	writeToScreen(messageData);
	console.log(messageData);

	if (messageData.messageType === "localServerInfoResponse") {
		messageData = JSON.parse(messageData.payloadData);
		mySessionId = document.getElementById("mySessionId");
		mySessionId.innerHTML = messageData.sessionId;
		myServerHost = document.getElementById("myServerHost");
		myServerHost.innerHTML = messageData.hostname;
		myServerId = document.getElementById("myServerId");
		myServerId.innerHTML = messageData.serverId;
		mySessionIdValue = messageData.sessionId;
		myServerIdValue = messageData.serverId;
		myHostNameValue = messageData.hostname;
		
		clearSessionListTable();
		tableElement = createHtmlTable("table table-hover table-condensed");
		addTableHeaderRow(tableElement, { data: ["Session Id", "Started", "Server Id", ""] });
		sessionList = messageData.sessionList;
		for (var i = 0; i < messageData.sessionList.length; i++) {
			tRow = addTableDataRow(tableElement, 
					{data: [ messageData.sessionList[i].sessionId, 
					         messageData.sessionList[i].started,
					         messageData.sessionList[i].serverId
			               ]});
			tdElement = document.createElement("td");
			chckBox = document.createElement("input");
			chckBox.className = "btn btn-default";
			chckBox.value = "...";
			chckBox.type = "button";
			chckBox.onclick = (function (index) {
				return function() { 
				sessionIdInputId = document.getElementById("sessionIdInputId");
				sessionIdInputId.value = sessionList[index].sessionId;
				};
			})(i);
			tdElement.appendChild(chckBox);
			tRow.appendChild(tdElement);
		}
		sessionListDiv.appendChild(tableElement);
		sessionCountTxt = document.getElementById("sessionCountTxt");
		sessionCountTxt.innerHTML = messageData.sessionList.length;
		if (messageData.sessionList.length > 0) {
			messageForm = document.getElementById("messageForm");
			messageForm.style.display = "block";
		}
	} else if (messageData.messageType === "systemEvent") {
		if (messageData.systemEventType === "userJoined" ||
				messageData.systemEventType === "userDisconnected" ||
				messageData.systemEventType === "userConnectionError" ) {
			id = id + 1;
			message = {
				sourceId : mySessionIdValue,
				targetId : "server",
				messageId : id.toString(),
				contextId : "",
				messageType : "getLocalServerInfo",
				returnCode : "",
				payloadData : ""
			}
			doSend(message);
		}
	}

}

function onError(evt) {
	writeToScreen({messageType: "localws", message: evt.data });
	disableControls();
	clearSessionListTable();
	hideMessageForm();
	clearActiveSessionCount();
}

function onClose(evt) {
	writeToScreen({messageType: "localws", message: evt.data });
	disableControls();
	clearSessionListTable();
	hideMessageForm();
	clearActiveSessionCount();
}

function doSend(message) {
	writeToScreen(message);
	websocket.send(JSON.stringify(message));
}

function writeToScreen(message) {
	messageLog = document.getElementById("messageLog");
	msgWraper = document.createElement("div");
	msgWraper.style.wordWrap = "break-word";
	msgWraper.role = "alert";
	if (message.messageType === "localws") {
		msgWraper.className = "alert alert-info";
		msgWraper.innerHTML = message.message;
	} else if (message.messageType === "localServerInfoResponse") {	
	} else if (message.messageType === "getLocalServerInfo") {	
	} else if (message.messageType === "systemEvent") {
		msgWraper.className = "alert alert-warning";
		msgWraper.innerHTML = message.systemEventType + " " + message.sessionId;
	} else if (message.messageType === "dataMessage") {
		msgWraper.className = "alert alert-success";
		payloadData = JSON.parse(message.payloadData);
		msgWraper.innerHTML = "Message [" + message.sourceId + "] / [" + payloadData.userId + "] wrote: <strong>" + payloadData.message; + "</strong>"
	} else if (message.messageType === "dataMessageAck") {	
	} else if (message.messageType === "ackOK") {	
	} else {
		msgWraper.className = "alert alert-success";
		msgWraper.innerHTML = JSON.stringify(message);
	}
	if (messageLog.firstChild) {
		messageLog.insertBefore(msgWraper, messageLog.firstChild);
	} else {
		messageLog.appendChild(msgWraper);
	}
}

function clearScreen(message) {
	messageLog = document.getElementById("messageLog");
	while (messageLog.firstChild) {
		messageLog.removeChild(messageLog.firstChild);
	}
}

function disableControls() {
	connectBtn = document.getElementById("btnConnect");
	connectBtn.disabled = false;
	disconnectBtn = document.getElementById("btnDisconnect");
	disconnectBtn.disabled = true;
	sendBtn = document.getElementById("btnSend");
	sendBtn.disabled = true;
	document.getElementById("mySessionId").innerHTML = "?";
	document.getElementById("myServerHost").innerHTML = "?";
	document.getElementById("myServerId").innerHTML = "?";
}

function clearSessionListTable() {
	sessionListDiv = document.getElementById("sessionListDiv");
	while (sessionListDiv.firstChild) {
		sessionListDiv.removeChild(sessionListDiv.firstChild);
	}
}

function hideMessageForm() {
	messageForm = document.getElementById("messageForm");
	messageForm.style.display = "none";
}

function validateInputForm() {
	result = true;
	if (!validateInputFieldNotEmpty("userIdFormGroup", "userIdInputId")) {
		result = false;
	}
	if (!validateInputFieldNotEmpty("sessionIdFormGroup", "sessionIdInputId")) {
		result = false;
	}
	if (!validateInputFieldNotEmpty("messageFormGroup", "messageInputId")) {
		result = false;
	}
	return result;
}

function clearActiveSessionCount() {
	sessionCountTxt = document.getElementById("sessionCountTxt");
	sessionCountTxt.innerHTML = "?";
}

