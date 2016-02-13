package models;

import pieces.Piece;
import play.libs.F.Callback;
import play.mvc.WebSocket;

public class WebSocketHandler extends WebSocket<String> {
	private Game game;
	private WebSocket.Out<String> out;
	public WebSocketHandler(Game game){
		this.game = game;
	
	}
	@Override
	public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
		this.out = out;
		game.addConnection(this);
		refreshBoard();
		
		in.onMessage(new Callback<String>() {

			@Override
			public void invoke(String msg) throws Throwable {
				String[] splitted = msg.split(" ");
				int fromRow = Integer.valueOf(splitted[0]);
				int fromCol = Integer.valueOf(splitted[1]);
				int toRow = Integer.valueOf(splitted[2]);
				int toCol = Integer.valueOf(splitted[3]);
				boolean moveMade = game.makeMove(fromRow, fromCol, toRow, toCol, false);
				out.write(moveMade+ "");
				
			}

		});
	}
	public void refreshBoard(){
		for(int i=0; i<8; ++i){
			for(int j=0; j<8; ++j){
				Piece p = game.getBoard()[i][j];
				int type;
				if(p!=null){
					type = p.getId();
					if(p.getColor() == Color.WHITE){
						type+=6;
					}
				} else {
					type = 12;
				}
				out.write(i*8+j + " " + type);
			}
		}
	}

}
