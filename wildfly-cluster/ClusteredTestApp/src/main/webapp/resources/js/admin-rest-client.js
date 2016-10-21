
var xmlhttp = new XMLHttpRequest();
var sessionList;

function getRootUri() {
	return (document.location.protocol == "http:" ? "http://" : " https://")
			+ (document.location.hostname == "" ? "localhost"
					: document.location.hostname) + ":"
			+ document.location.port;
}

xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		console.log(xmlhttp.responseText);
		messageData = JSON.parse(xmlhttp.responseText);
		if (messageData.messageType === "adminData") {
			if (messageData.authorized) {
				handleLoginPanelVisibility(true);
				serverIdLogoutTxt = document.getElementById("serverIdLogoutTxt");
				serverIdLogoutTxt.innerHTML = messageData.serverId;
				hostNameIdLogoutTxt = document.getElementById("hostNameIdLogoutTxt");
				hostNameIdLogoutTxt.innerHTML = messageData.hostname; 
				userIdLogoutTxt = document.getElementById("userIdLogoutTxt");
				userIdLogoutTxt.innerHTML = messageData.userId;
				loggedTimeIdLogoutTxt = document.getElementById("loggedTimeIdLogoutTxt");
				loggedTimeIdLogoutTxt.innerHTML = messageData.loginDate;
				getSessionList();
			} else {
				handleLoginPanelVisibility(false);
				serverIdLoginTxt = document.getElementById("serverIdLoginTxt");
				serverIdLoginTxt.innerHTML = messageData.serverId;
				hostNameLoginTxt = document.getElementById("hostNameLoginTxt");
				hostNameLoginTxt.innerHTML = messageData.hostname; 
				sessionListDiv = document.getElementById("sessionListDiv");
				while (sessionListDiv.firstChild) {
					sessionListDiv.removeChild(sessionListDiv.firstChild);
				}
			}
		} else if (messageData.messageType === "adminLogout" || messageData.messageType === "adminLogin") {
			getServerInfo();
		} else if (messageData.messageType === "activeSessionList") {	
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
					chckBox.value = "Stop";
					chckBox.type = "button";
					chckBox.onclick = (function (index) {
						return function() { 
							disconnectWsSession(sessionList[index].sessionId);
							window.setTimeout(getSessionList, 2000);
						};
					})(i);
					tdElement.appendChild(chckBox);
					tRow.appendChild(tdElement);
				}
				sessionListDiv.appendChild(tableElement);
				sessionCountTxt = document.getElementById("sessionCountTxt");
				sessionCountTxt.innerHTML = messageData.sessionList.length;
			} else {
				sessionCountTxt = document.getElementById("sessionCountTxt");
				sessionCountTxt.innerHTML = "0";
			}
		} else {
			console.log("unsupported message type");
		}
	}
}

function getServerInfo() {
	url = getRootUri() + "/ClusteredTestApp/ws/wsadmin/getadmindata";
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function loginAction() {
	userId = document.getElementById("userIdInputId").value;
	password = document.getElementById("passwordIdInputId").value;
	url = getRootUri() + "/ClusteredTestApp/ws/wsadmin/login?userId="
	          + encodeURIComponent(userId) + "&password="
	          + encodeURIComponent(password);
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function disconnectWsSession(sessionId) {
	url = getRootUri() + "/ClusteredTestApp/ws/wsadmin/disconnectwssession?wsSessionId="
	          + encodeURIComponent(sessionId); 
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function logoutAction() {
	url = getRootUri() + "/ClusteredTestApp/ws/wsadmin/logout";
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function getSessionList() {
	url = getRootUri() + "/ClusteredTestApp/ws/wsrest/getsessions";
	console.log("getSessionList: " + url);
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function handleLoginPanelVisibility(loggedIn) {
	logoutPanel = document.getElementById("logoutPanel");
	loginPanel = document.getElementById("loginPanel");
	activeSessionsPanel = document.getElementById("activeSessionsPanel");
	if (loggedIn) {
		loginPanel.style.display = "none";
		loginPanel.style.visibility = "hidden";
		logoutPanel.style.display = "block";
		logoutPanel.style.visibility = "visible";
		activeSessionsPanel.style.display = "block";
		activeSessionsPanel.style.visibility = "visible";
	} else {
		loginPanel.style.display = "block";
		loginPanel.style.visibility = "visible";
		logoutPanel.style.display = "none";
		logoutPanel.style.visibility = "hidden";
		activeSessionsPanel.style.display = "none";
		activeSessionsPanel.style.visibility = "hidden";
	}
} 

window.onload = function() {
	getServerInfo();
}

