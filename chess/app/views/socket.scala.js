@(gameId: Long)
$(function() {
	var moves = 0;
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var socket = new WS("@routes.Application.socketWs(gameId).webSocketURL(request, true)")

    var getImgUrl = function(type, color) {
    	return '/assets/images/' + type + '_' + color +'.png';	
    }
    var getColor = function(x, y) {
    	if((x+y)%2==0){
    		return 1;
    	} else {
    		return 2;
    	}
    }
    var changeImageType = function(x, y, type){
    	var color = getColor(x,y);
    	var url = getImgUrl(type, color);
    	jQuery("#" + (x*8 + y)).attr("src",url);
    }
    

    var receiveEvent = function(event) {
    	var id = event.data.split(" ")[0];
    	var type = event.data.split(" ")[1];
    	if(id == -1){
    		jQuery("#moves_list").prepend("<li class='list-group-item'>"+event.data.substring(2)+ '</li>');
    	} else {
    		changeImageType(Math.floor(id/8), id%8, type);    		
    	}
    }
    socket.onmessage = receiveEvent
    
    var makeMove = function(fromRow, fromColumn, toRow, toColumn) {
    	var msg = "" + fromRow + " " + fromColumn + " " + toRow + " " + toColumn;
    	socket.send(msg);
    } 
    
    var table = $('<table cellspacing="0" cellpadding="0"></table>').addClass('foo');
    
    for(i=0; i<8; i++){
        var row = $('<tr></tr>');
        for(j=0; j<8; ++j){
        	var bl = $('<td></td>');
        	var imgType = 12;
        	var color = getColor(i,j);
        	 
        	var img = $('<img src="' + getImgUrl(imgType, color) + '" alt="" >');
        	img.attr('id', i*8 + j);
        	img.addClass("square");
        	bl.append(img);
        	row.append(bl);
        }
        
        table.append(row);
    }

    $('#here_table').append(table);
    var currentClicked;
    $('td').click(function(){
        var colIndex = $(this).parent().children().index($(this));
        var rowIndex = $(this).parent().parent().children().index($(this).parent());
        var emptySquareImg = getImgUrl(12, getColor(rowIndex, colIndex));
        if(currentClicked === undefined){
        	if (emptySquareImg != $(this).attr('src')){
        		currentClicked = $(this);        
        	    $('.selected').removeClass('selected');
        	    $(this).addClass('selected');
        	}
        } else {
        	var fromRow = currentClicked.parent().parent().children().index(currentClicked.parent());
        	var fromCol = currentClicked.parent().children().index(currentClicked);
        	makeMove(fromRow, fromCol, rowIndex, colIndex);
        	currentClicked = undefined;
        	$('.selected').removeClass('selected');
        	//alert("making move " + fromRow + " " + fromCol + " " + rowIndex + " " + colIndex);
        }
       // alert(currentClicked);
    });
    
    $("td").each(function() {
        var id = $(this).attr("id");
       // alert("ASD")
        // compare id to what you want
    });
	
    
})
