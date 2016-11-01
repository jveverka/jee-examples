
var messageHandler;
var urlPrefix = '../../';
var uri = 'home';
var userName;
var showWsDisconnectDialog = true;

var testRequestCounter = 0;

function startApplication() {
	console.log("start-application ...");
	checkAuthorizedSession();
	window.onbeforeunload = onReloadFunction;
}

function onReloadFunction () {
	console.log("onReloadFunction");
	webSocket.onclose = function () {};
	webSocketDisconnect();
}

function setElementVisible(elementItSelector, isVisible) {
    if (isVisible) {
 	   $(elementItSelector).css({ "visibility":"visible"});
 	   $(elementItSelector).css({ "display":"block"});
 	} else {
 	   $(elementItSelector).css({"visibility":"hidden"});
 	   $(elementItSelector).css({"display":"none"});
 	}
}

function showDataPanel(name) {
	console.log("showDataPanel: " + name);
	if (uri === name) return;
	if (uri ==='home') {
		setElementVisible("#homeDataTableWrapper", false);
	} else if (uri ==='admin') {
		setElementVisible("#adminDataTableWrapper", false);
		shutdownAdminUI();
	} else if (uri ==='devices') {
		setElementVisible("#devicesDataTableWrapper", false);
		shutdownDevicesUI();
	} else if (uri ==='chat') {
		setElementVisible("#chatDataTableWrapper", false);
		shutdownChatUI();
	} else if (uri ==='test') {
		setElementVisible("#testInfoPanel", false);
		shutdownTestUI();
	}
	if (name ==='home') {
		setElementVisible("#homeDataTableWrapper", true);
		uri = 'home';
	} else if (name ==='admin') {
		setElementVisible("#adminDataTableWrapper", true);
		uri = 'admin'; initAdminUI();
	} else if (name ==='devices') {
		setElementVisible("#devicesDataTableWrapper", true);
		uri = 'devices'; initDevicesUI();
	} else if (name ==='chat') {
		setElementVisible("#chatDataTableWrapper", true);
		uri = 'chat'; initChatUI();
	} else if (name ==='test') {
		setElementVisible("#testInfoPanel", true);
		uri = 'test'; initTestUI();
	} 
}

function checkAuthorizedSession() {
	console.log("checkAuthorizedSession");
	sendAjaxPOST(urlPrefix + "ws/useraccess/isValidSession", { }, onSessionAuthorizedOK, onSessionAuthorizedFail, onSessionAuthorizedFail );
}

function onSessionAuthorizedOK() {
	console.log("onSessionAuthorizedOK");
	setElementVisible('#dataContainer',true);
	setElementVisible('#loginContainer',false);
	webSocketConnect(onWsOpenHandler, onWsMessageHandler, onWsErrorHandler, onWsCloseHandler);
}

function onSessionAuthorizedFail() {
	console.log("onSessionAuthorizedFail");
	setElementVisible('#dataContainer',false);
	setElementVisible('#loginContainer',true);
	webSocketDisconnect();
}

function login() {
	userName = document.getElementById("userNameInputId").value;
	password = document.getElementById("passwordInputId").value; 
	console.log("loginAction for " + userName);
	sendAjaxPOST(urlPrefix + "ws/useraccess/login", { userName: userName, password: password }, onLoginOk, undefined, undefined );	
}

function logout() {
	console.log("logoutAction");
	showWsDisconnectDialog = false;
	sendAjaxPOST(urlPrefix + "ws/useraccess/logout", {}, onLogoutOk, undefined, undefined );	
}

function onLoginOk(data) {
	console.log("onLoginOk");
	setElementVisible('#dataContainer',true);
	setElementVisible('#loginContainer',false);
	showDataPanel('home');
	webSocketConnect(onWsOpenHandler, onWsMessageHandler, onWsErrorHandler, onWsCloseHandler);
}

function onLogoutOk(data) {
	console.log("onLogoutOk");
	setElementVisible('#dataContainer',false);
	setElementVisible('#loginContainer',true);
	webSocketDisconnect();
	location.reload();
}

function logoutOnWSDisconnect() {
	console.log("logoutOnWSDisconnect");
	sendAjaxPOST(urlPrefix + "ws/useraccess/logout", {}, onLogoutForced, onLogoutForced, onLogoutForced );	
}

function onLogoutForced() {
	location.reload();
}

/**
 * web socket stuff 
 */

function onWsOpenHandler() {
	console.log("onWsOpenHandler");
	$('#userNameHolder').text(userName);
	$('#httpSessionIdHolder').text(getHttpSessionId());
} 

function onWsMessageHandler(message) {
	//console.log("onWsMessageHandler");
	dataMessage = JSON.parse(message.data);
	if (dataMessage.echoData) {
		webSocketSendMessage(dataMessage);
		testRequestCounter++;
		document.getElementById("testCounterId").innerHTML = testRequestCounter;
	} else if (dataMessage.loginResponse) {
		$('#wsSessionIdHolder').text(dataMessage.loginResponse.wsSessionId);
	} else if (typeof messageHandler === 'function' ) {
		messageHandler(dataMessage);
	}
}

function onWsErrorHandler() {
	console.log("onWsErrorHandler");
	$('#offlineModalDialog').modal('show');
}

function onWsCloseHandler() {
	console.log("onWsCloseHandler");
	if (showWsDisconnectDialog) {
		$('#offlineModalDialog').modal('show');
	}
}

