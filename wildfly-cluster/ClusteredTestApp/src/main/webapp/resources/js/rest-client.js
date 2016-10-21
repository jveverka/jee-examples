var messageId = 0;
var xmlhttp = new XMLHttpRequest();
var serverId;
var hostname;
var sessionList;

xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		console.log(xmlhttp.responseText);
		messageData = JSON.parse(xmlhttp.responseText);
		if (messageData.messageType === "activeSessionList") {
			sessionListDiv = document.getElementById("sessionListDiv");
			while (sessionListDiv.firstChild) {
				sessionListDiv.removeChild(sessionListDiv.firstChild);
			}
			if (messageData.sessionList.length > 0) {
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
				messageForm = document.getElementById("messageForm");
				messageForm.style.visibility = "visible";
			} else {
				sessionCountTxt = document.getElementById("sessionCountTxt");
				sessionCountTxt.innerHTML = "0";
				messageForm = document.getElementById("messageForm");
				messageForm.style.visibility = "hidden";
			}
		} else if (messageData.messageType === "serverInfo") {
			serverIdTxt = document.getElementById("serverIdTxt");
			serverIdTxt.innerHTML = messageData.serverInfo.serverId;
			serverId = messageData.serverInfo.serverId;
			hostNameTxt = document.getElementById("hostNameTxt");
			hostNameTxt.innerHTML = messageData.serverInfo.hostname;
			hostname = messageData.serverInfo.hostname;
		} else if (messageData.messageType === 'dataMessageAck') {
			responsePayload = JSON.parse(messageData.payloadData);
			syncResponseTxt = document.getElementById("syncResponseTxt");
			syncResponseTxt.innerHTML = messageData.returnCode + " "
					+ responsePayload.message;
			responseTxt = document.getElementById("responseTxt");
			responseTxt.innerHTML = "OK";
		} else if (messageData.messageType === 'replyMessage') {
			syncResponseTxt = document.getElementById("syncResponseTxt");
			syncResponseTxt.innerHTML = "?";
			responseTxt = document.getElementById("responseTxt");
			responseTxt.innerHTML = messageData.returnCode + " " + messageData.message;
		} else {
			console.log("unsupported message type");
		}
	}
}

function getRootUri() {
	return (document.location.protocol == "http:" ? "http://" : " https://")
			+ (document.location.hostname == "" ? "localhost"
					: document.location.hostname) + ":"
			+ document.location.port;
}

function getSessionList() {
	url = getRootUri() + "/ClusteredTestApp/ws/wsrest/getsessions";
	console.log("getSessionList: " + url);
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function getServerInfo() {
	url = getRootUri() + "/ClusteredTestApp/ws/wsrest/getserverinfo";
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function getSendDataMessageAsync() {
	if (!validateInputForm()) {
		return;
	}
	userId = document.getElementById("userIdInputId").value;
	sessionId = document.getElementById("sessionIdInputId").value;
	contextId = "0";
	messageText = document.getElementById("messageInputId").value;
	url = getRootUri() + "/ClusteredTestApp/ws/wsrest/senddatamessage?userId="
			+ encodeURIComponent(userId) + "&sessionId="
			+ encodeURIComponent(sessionId) + "&messageId="
			+ encodeURIComponent(messageId) + "&contextId="
			+ encodeURIComponent(contextId) + "&message="
			+ encodeURIComponent(messageText);
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
	messageId++;
}

function getSendDataMessageSync() {
	if (!validateInputForm()) {
		return;
	}
	userId = document.getElementById("userIdInputId").value;
	sessionId = document.getElementById("sessionIdInputId").value;
	contextId = "0";
	messageText = document.getElementById("messageInputId").value;
	url = getRootUri()
			+ "/ClusteredTestApp/ws/wsrest/senddatamessagesync?userId="
			+ encodeURIComponent(userId) + "&sessionId="
			+ encodeURIComponent(sessionId) + "&messageId="
			+ encodeURIComponent(messageId) + "&contextId="
			+ encodeURIComponent(contextId) + "&message="
			+ encodeURIComponent(messageText);
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
	messageId++;
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

window.onload = function() {
	getServerInfo();
}

