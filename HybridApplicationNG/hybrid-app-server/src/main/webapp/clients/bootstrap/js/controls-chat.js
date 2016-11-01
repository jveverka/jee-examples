
var chatMessage;
var selectedChatId;

function initChatUI() {
	console.log("initChatUI");
	messageHandler = chatMessageHandler;
	webSocketTopicSubscribe('/chat');
	webSocketSendMessage( { chatListRequest: {}} );
}

function shutdownChatUI() {
	console.log("shutdownChatUI");
	if (selectedChatId != undefined) {
		webSocketTopicUnsubscribe('/chat/' + selectedChatId);
	}
	webSocketTopicUnsubscribe('/chat');
}

function chatMessageHandler(message) {
	chatMessage = message;
	if (chatMessage.chatListResponse) {
		console.log("chatListResponse");
		showChatList(chatMessage.chatListResponse);
	} else if (chatMessage.chatHistoryResponse) {
		console.log("chatHistoryResponse");
		showChatHistory(chatMessage.chatHistoryResponse);
	} else if (chatMessage.chatPublishEvent) {
		console.log("chatPublishEvent");
		showPublishedMessage(chatMessage.chatPublishEvent.message);
	}
}

function submitChatMessage() {
	console.log("submitChatMessage");
	actualChatId = document.getElementById('inputChatId').value;
	if (selectedChatId != actualChatId) {
		if (selectedChatId != undefined) {
			webSocketTopicUnsubscribe('/chat/' + selectedChatId);
		}
		selectedChatId = actualChatId;
		webSocketTopicSubscribe('/chat/' + selectedChatId);
		webSocketSendMessage({ chatHistoryRequest: { chatId: selectedChatId } }); 
		$('#inputChatList').val(selectedChatId);
		doSendMessage();
	} else {
		doSendMessage();
	}
}

function doSendMessage() {
	timeStamp = new Date().getTime();
	messageText = document.getElementById('inputChatMessageId').value;
	webSocketSendMessage( 
			{ chatPublishEvent: 
			{ chatId: selectedChatId, 
				message: { timeStamp: timeStamp, fromUser: userName , message: messageText } 
		} });
}

function onChatIdSelect() {
	console.log("onChatIdSelect");
	if (selectedChatId === undefined) {
		webSocketTopicUnsubscribe('/chat/' + selectedChatId);
	}
	selectedChatId = $("#inputChatList").val();
	$('#inputChatId').val(selectedChatId);
	webSocketTopicSubscribe('/chat/' + selectedChatId);
	webSocketSendMessage({ chatHistoryRequest: { chatId: selectedChatId } }); 
}

function showChatList(chatListResponse) {
	console.log("showChatList: ");
	$('#inputChatList').empty();
	for (var i=0; i<chatListResponse.chatId.length; i++) {
		option = document.createElement('option');
		option.innerHTML = chatListResponse.chatId[i];
		option.value = chatListResponse.chatId[i];
		$('#inputChatList').append(option);
	}
	if (chatListResponse.chatId && 
			chatListResponse.chatId.length > 0 && 
			selectedChatId === undefined) {
		selectedChatId = chatListResponse.chatId[0];
		$('#inputChatId').val(chatListResponse.chatId[0]);
		webSocketTopicSubscribe('/chat/' + selectedChatId);
		webSocketSendMessage({ chatHistoryRequest: { chatId: selectedChatId } }); 
	}
	$('#inputChatList').val(selectedChatId);
}

function showChatHistory(chatHistoryResponse) {
	console.log("showChatHistory:");
	$('#chatMessages').empty();
	for (var i=0; i<chatHistoryResponse.messages.length; i++) {
		showPublishedMessage(chatHistoryResponse.messages[i]);
	}
}

function showPublishedMessage(message) {
	console.log("showPublishedMessage:");
	row = document.createElement('tr');
	td = document.createElement('td');
	td.innerHTML = message.timeStamp;
	row.appendChild(td);
	td = document.createElement('td');
	td.innerHTML = message.fromUser;
	row.appendChild(td);
	td = document.createElement('td');
	td.innerHTML = message.message;
	row.appendChild(td);
	$('#chatMessages').append(row);
}
