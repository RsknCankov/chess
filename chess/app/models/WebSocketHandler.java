package models;

import play.libs.F.Callback;
import play.mvc.WebSocket;

public class WebSocketHandler extends WebSocket<String> {
	private Game game;
	public WebSocketHandler(Game game){
		this.game = game;
	}
	@Override
	public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
		in.onMessage(new Callback<String>() {

			@Override
			public void invoke(String msg) throws Throwable {
				String[] splitted = msg.split(" ");
				int fromRow = Integer.valueOf(splitted[0]);
				int fromCol = Integer.valueOf(splitted[1]);
				int toRow = Integer.valueOf(splitted[2]);
				int toCol = Integer.valueOf(splitted[3]);
				boolean moveMade = game.makeMove(fromRow, fromCol, toRow, toCol);
				out.write(moveMade+ "");
				
			}

		});
	}
}
