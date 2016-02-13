package pieces;

import java.util.ArrayList;
import java.util.List;

import models.Color;
import models.Game;

public class Pawn extends Piece {

	public Pawn(Game game, Color color, int currRow, int currColumn) {
		super(game, color, currRow, currColumn);
	}

	@Override
	public List<Move> getPossibleMoves(int toRow, int toColumn) {
		List<Move> moves = new ArrayList<Move>();
		moves.add(new SingleSquareMove(color.getPawnDirection(), 0, toRow, toColumn, 1, this));
		moves.add(new TwoSquareMove(toRow, toColumn, this));
		moves.add(new CaptureMove(-1, toRow, toColumn, this));
		moves.add(new CaptureMove(1, toRow, toColumn, this));
		return moves;
	}
	class SingleSquareMove extends SimpleMove{

		public SingleSquareMove(int dx, int dy, int toRow, int toColumn, int allowedSteps, Piece piece) {
			super(dx, dy, toRow, toColumn, allowedSteps, piece);
		}
		@Override
		protected boolean satisfiesConditions() {
			return game.getBoard()[toRow][toColumn] == null;
		}
		
	}
	class TwoSquareMove extends SimpleMove {

		public TwoSquareMove(int toRow, int toColumn, Piece piece) {
			super(piece.color.getPawnDirection(), 0, toRow, toColumn, 2, piece);
		}
		@Override
		protected boolean satisfiesConditions() {
			return game.getBoard()[toRow][toColumn]==null && Pawn.this.getCurrentRow() == Pawn.this.color.getSecondRow();
		}
	}
	class CaptureMove extends SimpleMove {

		public CaptureMove(int dy, int toRow, int toColumn, Piece piece) {
			super(piece.color.getPawnDirection(),dy , toRow, toColumn, 1, piece);
		}
		@Override
		protected boolean satisfiesConditions() {
			Piece pieceToTake = game.getBoard()[toRow][toColumn];
			return pieceToTake!= null && pieceToTake.color!= color;
		}
		
	}
	class EnPassant extends SimpleMove {

		public EnPassant(int dy, int toRow, int toColumn, Piece piece) {
			super(piece.color.getPawnDirection(), dy, toRow, toColumn, 1, piece);
		}
		@Override
		protected boolean satisfiesConditions() {
			if(game.getMovesHistory().size()==0) return false;
			Move lastMove = game.getMovesHistory().get(game.getMovesHistory().size()-1);
			boolean lastMoveValid = lastMove instanceof TwoSquareMove && lastMove.toRow == piece.getCurrentRow() && lastMove.toColumn == toColumn;
			return game.getBoard()[toRow][toColumn]==null && lastMoveValid;
		}
		@Override
		public List<MoveEffect> getEffects() {
			List<MoveEffect> effects = super.getEffects();
			effects.add(new MoveEffect() {
				
				@Override
				public void apply(Game game) {
					game.getBoard()[toRow-getDx()][toColumn] = null;
				}
			});
			return effects;
		}
		
	}
	@Override
	public Piece clone() {
		return new Pawn(getGame(), color, currentRow, currentColumn);
	}

	@Override
	public int getId() {
		return 5;
	}
}
