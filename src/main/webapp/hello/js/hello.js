$(function() {
	$.get("/expense",function(data,status){
		alert("Data: " + data + "\nStatus: " + status);
	});
})