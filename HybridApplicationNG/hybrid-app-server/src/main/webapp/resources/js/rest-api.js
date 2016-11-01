
function getHttpSessionId() {
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

function sendAjaxPOST(urlString, requestData, onDataOkHandler, onErrorHandler, onForbiddenFunction) {
    console.log("sendAjaxPOST ...");
    sessionId = getHttpSessionId();
    jQuery.ajax({
        url: urlString,
        type: "POST",
        data: JSON.stringify(requestData),
        success: function (data) {
            console.log("sendAjaxPOST: OK");
            if (typeof onDataOkHandler === 'function' ) { onDataOkHandler(data);}
        },
        statusCode: { 
        	403: function() { 
        		 if (typeof onForbiddenFunction === 'function' ) { onForbiddenFunction(); } 
        		 } 
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("sendAjaxPOST: ERROR, " + textStatus + ", " + errorThrown);
            if (typeof onErrorHandler === 'function' ) { onErrorHandler(); }
        },
        contentType: 'application/json',
    });
}
