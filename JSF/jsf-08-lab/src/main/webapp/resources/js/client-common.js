
function setRowCol(rowId, colId, data) {
	console.log("setRowCol: [" + rowId + "," + colId + "]=" + data);
	$(".colId").val(colId); 
	$(".rowId").val(rowId);
	$(".dataId").val(data);
}
