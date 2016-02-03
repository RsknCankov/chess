package pieces;

import java.util.ArrayList;
import java.util.List;

import akka.routing.FromConfig;
import models.Color;
import models.Game;

public class King extends Piece {

	public King(Game game, Color color, int currRow, int currColumn) {
		super(game, color, currRow, currColumn);
	}

	@Override
	public List<Move> getPossibleMoves(int toRow, int toColumn) {
		int dx[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int dy[] = { 1, 1, 0, -1, -1, -1, 0, 1 };
		List<Move> moves = new ArrayList<>();
		for (int i = 0; i < dx.length; ++i) {
			moves.add(new SimpleMove(dx[i], dy[i], toRow, toColumn, 1, this));
		}
		moves.add(new CastlingMove(0, 2, toRow, toColumn, 1, this));
		return moves;
	}
	class CastlingMove extends SimpleMove{
		public CastlingMove(int dx, int dy, int toRow, int toColumn, int allowedSteps, Piece piece) {
			super(dx, dy, toRow, toColumn, allowedSteps, piece);
		}
		@Override
		protected boolean satisfiesConditions() {
			if(!(currentRow == toRow && toRow == piece.getColor().getFirstRow()&& Math.abs(currentColumn - toColumn)==2 )){
				return false;
			}
			int rookColumn;
			if(toColumn > currentColumn){
				rookColumn = 7;
			} else {
				rookColumn = 0;
			}
					
			boolean werentMoved = !game.wasMoved(currentRow, currentColumn) && !game.wasMoved(currentRow, rookColumn);
			boolean isPathClear = true;
			for(int c = Math.min(rookColumn, currentColumn)+1; c<Math.max(rookColumn, currentColumn);++c){
				if(game.getBoard()[toRow][c] != null){
					isPathClear = false;
				}
			}
			return werentMoved && isPathClear;
		}
		@Override
		public List<MoveEffect> getEffects() {
			List<MoveEffect> effects = super.getEffects();
			effects.add(new MoveEffect() {
				@Override
				public void apply() {
					if(toColumn > currentColumn){
						game.getBoard()[toRow][toColumn-1] = game.getBoard()[toRow][7];
						game.getBoard()[toRow][7] = null;
					} else {
						game.getBoard()[toRow][toColumn+1] = game.getBoard()[toRow][0];
						game.getBoard()[toRow][0] = null;
					}
				}
			});
			return effects;
		}
	}

}
