package models;

import pieces.Move;
import pieces.Piece;
import play.libs.F.Callback;
import play.mvc.WebSocket;
/* Handles communication between the server and the clients during a game */
public class WebSocketHandler extends WebSocket<String> {
	private Game game;
	private WebSocket.Out<String> out;
	private String username;
	public WebSocketHandler(Game game, String username){
		this.game = game;
		this.username = username;
	
	}
	/* When a client connects he gets the current state of the board
	 * and subscribes to future updates
	 */
	@Override
	public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
		this.out = out;
		game.addConnection(this);
		refreshBoard();
		for(Move move : game.getMovesHistory()){
			sendMessage(move.toString());
		}
		in.onMessage(new Callback<String>() {

			@Override
			public void invoke(String msg) throws Throwable {
				if(game.usernameCanMakeMove(username)){
					String[] splitted = msg.split(" ");
					int fromRow = Integer.valueOf(splitted[0]);
					int fromCol = Integer.valueOf(splitted[1]);
					int toRow = Integer.valueOf(splitted[2]);
					int toCol = Integer.valueOf(splitted[3]);
					game.makeMove(fromRow, fromCol, toRow, toCol, false);
				}				
			}

		});
	}
	/* Sends the current state of the board to the client */
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
	/* Sends a text message to the client */
	public void sendMessage(String msg) {
		out.write("-1 " + msg);
	}

}
