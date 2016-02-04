package pieces;

import java.util.ArrayList;
import java.util.List;

import models.Color;
import models.Game;

public class Rook extends Piece {

	public Rook(Game game, Color color, int currRow, int currColumn) {
		super(game, color, currRow, currColumn);
	}

	@Override
	public List<Move> getPossibleMoves(int toRow, int toColumn) {
		int dx[] = {0, 1, 0, -1};
		int dy[] = {1, 0, -1, 0};
		List<Move> moves = new ArrayList<>();
		for (int i = 0; i < dx.length; ++i) {
			moves.add(new SimpleMove(dx[i], dy[i], toRow, toColumn, 8, this));
		}
		return moves;
	}

	@Override
	public Piece clone() {
		return new Rook(getGame(), color, currentRow, currentColumn);
	}

	 
}
