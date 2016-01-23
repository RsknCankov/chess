package pieces;

import java.util.ArrayList;
import java.util.List;

import models.Color;
import models.Game;

public class Bishop extends Piece {

	public Bishop(Game game, Color color, int currRow, int currColumn) {
		super(game, color, currRow, currColumn);
	}

	@Override
	public List<Move> getPossibleMoves(int toRow, int toColumn) {
		int dx[] = {-1, 1, 1, -1};
		int dy[] = {1, 1, -1, -1};
		List<Move> moves = new ArrayList<>();
		for(int i=0; i<dx.length; ++i){
			moves.add(new SimpleMove(dx[i], dy[i], toRow, toColumn, 8, this));
		}
		return moves;
	}

	
}
