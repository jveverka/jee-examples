/**
 * 
 */

function createHtmlElement(element, style) {
	el = document.createElement(element);
	el.className = style;
	return el;
}

function createHtmlTable(style) {
	tableEl = createHtmlElement("table", style);
	theadEl = document.createElement("thead");
	tbodyEl = document.createElement("tbody");
	tableEl.appendChild(theadEl);
	tableEl.appendChild(tbodyEl);
	return tableEl;
}

function addTableHeaderRow(tableElement, rowData) {
	hRow = document.createElement("tr");
	for (var i=0; i<rowData.data.length; i++) {
		thElement = document.createElement("th");
		thElement.innerHTML = rowData.data[i];
		hRow.appendChild(thElement);
	}
	bodyElements = tableElement.getElementsByTagName("thead");
	if (bodyElements != undefined && bodyElements.length > 0) {
		bodyElements[0].appendChild(hRow);
	} else {
		tableElement.appendChild(hRow);
	}
	return hRow;
}

function addTableDataRow(tableElement, rowData) {
	hRow = document.createElement("tr");
	for (var i=0; i<rowData.data.length; i++) {
		thElement = document.createElement("td");
		thElement.innerHTML = rowData.data[i];
		hRow.appendChild(thElement);
	}
	bodyElements = tableElement.getElementsByTagName("tbody");
	if (bodyElements != undefined && bodyElements.length > 0) {
		bodyElements[0].appendChild(hRow);
	} else {
		tableElement.appendChild(hRow);
	}
	return hRow;
}

function validateInputFieldNotEmpty(formGroupId, inputFieldId) {
	result = true;
	inputFieldText = document.getElementById(inputFieldId).value;
	ifg = document.getElementById(formGroupId);
	if (inputFieldText === undefined || inputFieldText === "") {
		ifg.className = "form-group has-error";
		result = false;
	} else {
		ifg.className = "form-group";
	}
	return result;
}

