
var initialData;

/**
 * ws/dataws/getInitalData
 * getInitialData('ws/dataws/getInitalData',undefined);
 * @param urlString
 * @param resultHandlerFunction
 */
function getInitialData(urlString, resultHandlerFunction) {
    jQuery.ajax({
        url: urlString,
        type: "POST",
        data: JSON.stringify({}),
        success: function (data) {
            console.log("getInitialData: OK: " + data.returnCode + " " + data.data);
            if (typeof resultHandlerFunction === 'function') {
            	resultHandlerFunction(true, data);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("getInitialData: ERROR: " + textStatus + ", " + errorThrown);
            if (typeof resultHandlerFunction === 'function') {
            	resultHandlerFunction(false, textStatus);
            }
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
    });
}

/**
 * ws/dataws/updateData
 * updateData('ws/dataws/updateData',0,0,'yy',undefined);
 * @param urlString
 * @param resultHandlerFunction
 */
function updateData(urlString, row, col, data, resultHandlerFunction) {
    jQuery.ajax({
        url: urlString,
        type: "POST",
        data: JSON.stringify({ row: row, col: col, data: data}),
        success: function (data) {
            console.log("updateData: OK: " + data.returnCode + " " + data.data);
            if (typeof resultHandlerFunction === 'function') {
            	resultHandlerFunction(true, data);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("updateData: ERROR: " + textStatus + ", " + errorThrown);
            if (typeof resultHandlerFunction === 'function') {
            	resultHandlerFunction(false, textStatus);
            }
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
    });
}

function initialResultHandlerFunction(dataOk, data) {
	console.log("initialResultHandlerFunction: " + dataOk);
	if (dataOk) {
		console.log("data:" + data);
		initialData = data;
		tableContainer = document.getElementById("tablePlaceHolder");
		while (tableContainer.firstChild) {
			tableContainer.removeChild(tableContainer.firstChild);
		}
		dataTable = document.createElement("table");
		for (var i=0; i<data.data.length; i++) {
			tr = document.createElement("tr");
			for (var j=0; j<data.data[i].length; j++) {
				td = document.createElement("td");
				td.innerHTML = data.data[i][j];
				td.onclick = (function(rowId, colId, data) {return function() { setRowCol(rowId, colId, data);};})(i,j,data.data[i][j]);
				tr.appendChild(td); 
			}
			dataTable.appendChild(tr); 
		}
		tableContainer.appendChild(dataTable); 
	}
}

window.onload = function() {
	getInitialData('../ws/dataws/getInitalData',initialResultHandlerFunction);
}

function updateHandler(dataOk, data) {
	console.log("updateHandler: " + dataOk);
	if (dataOk) {
		getInitialData('../ws/dataws/getInitalData',initialResultHandlerFunction);
	}
}

function setValue() {
	row = document.getElementsByClassName('rowId')[0].value;
	col = document.getElementsByClassName('colId')[0].value;
	data = document.getElementsByClassName('dataId')[0].value;
	console.log("setValue [" + row + "," + col + "]=" + data);
	updateData('../ws/dataws/updateData', row, col, data, updateHandler);
}
