
var dataMessage;

/**
 * get current JSF session id from browser cookie
 * @returns JSF session Id if found or undefined
 */
function getJsfSessionId() {
    var name = "JSESSIONID=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        cookieName = ca[i].substring(0, ca[i].indexOf("=")).trim();
        if (cookieName === "JSESSIONID") {
            cookieValue = ca[i].substring(ca[i].indexOf("=") + 1, ca[i].lenght);
            return cookieValue.substring(0, cookieValue.indexOf("."));
        }
    }
    return undefined;
}

/**
 * 
 * @param urlString
 */
function sendGetDataRequest(urlString) {
    sessionId = getJsfSessionId();
    jQuery.ajax({
        url: urlString,
        type: "POST",
        data: JSON.stringify({sessionId: sessionId}),
        success: function (data) {
            console.log("sendGetDataRequest: " + data.returnCode + " " + data.message + " sessionTimeout: " + data.sessionTimeout);
            dataMessage = data;
            document.getElementById("timerDisplay").innerHTML = data.sessionTimeout;
            if (data.sessionTimeout < 0) {
            	logoutAction();
            	//location.reload(); 
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("sendGetDataRequest: ERROR: " + textStatus + ", " + errorThrown);
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
    });
}

function getDataFromServer() {
	sendGetDataRequest('../ws/webservice/getdata');
}

// var myVar=setInterval(function () {myTimer()}, 1000);
setInterval(function () {getDataFromServer()}, 3000);

