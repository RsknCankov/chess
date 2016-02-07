@(gameId: Long)
$(function() {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var socket = new WS("@routes.Application.socketWs(gameId).webSocketURL(request)")

    var receiveEvent = function(event) {
        $("#ping").html(event.data);
    }

    socket.onmessage = receiveEvent
    
    $("#btn" ).click(function() {
    		var val = $('#txt').val();
    		socket.send(val);
    	});
})
