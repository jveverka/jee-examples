
var webSocket;

function getWebSocketURL() {
	protocol = "ws";
	if (window.location.protocol === "https:") {
		protocol = "wss";
	}
	applName = window.location.pathname.substring(1);
	applName = applName.substring(0,applName.indexOf('/'));
	if (applName === 'public' || applName === 'views' ||  applName === 'admin') {
		return protocol + "://" + window.location.host + "/ws/wsendpoint";
	}
	return protocol + "://" + window.location.host + "/" + applName + "/ws/wsendpoint";
}

function webSocketConnect(onOpenHandler, onMessageHandler, onErrorHandler, onCloseHandler) {
	webSocket = new WebSocket(getWebSocketURL());
	webSocket.onopen = onOpenHandler;
	webSocket.onmessage = onMessageHandler;
	webSocket.onerror = onErrorHandler;
	webSocket.onclose = onCloseHandler;
}

function webSocketSendMessage(message) {
	webSocket.send(JSON.stringify(message));
}

function webSocketDisconnect() {
	if (webSocket != undefined) {
		webSocket.close();
	}
}

function webSocketTopicSubscribe(topicId) {
	dataMessage = { topicSubscribe : { topicId: topicId } };
	webSocketSendMessage(dataMessage);
}

function webSocketTopicUnsubscribe(topicId) {
	dataMessage = { topicUnsubscribe : { topicId: topicId } };
	webSocketSendMessage(dataMessage);
}

function webSocketTopicPublish(topicId, message) {
	dataMessage = { topicPublishMessage : { topicId: topicId, message: message } };
	webSocketSendMessage(dataMessage);
}

