package pieces;

import java.util.List;

import models.Color;
import models.Game;

public abstract class Move {
	private int dx;
	private int dy;
	private int allowedSteps;
	protected Piece piece;
	protected int toRow;
	protected int toColumn;
	protected Game game;
	private int fromRow;
	private int fromColumn;

	public Move(int dx, int dy, int toRow, int toColumn, int allowedSteps, Piece piece) {
		this.dx = dx;
		this.dy = dy;
		this.allowedSteps = allowedSteps;
		this.piece = piece;
		this.toRow = toRow;
		this.toColumn = toColumn;
		this.game = piece.getGame();
		this.fromRow = piece.getCurrentRow();
		this.fromColumn = piece.getCurrentColumn();
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getAllowedSteps() {
		return allowedSteps;
	}

	public abstract List<MoveEffect> getEffects();

	protected final boolean canReachEnd() {
		if (piece == null)
			return false;
		int currRow = piece.getCurrentRow() + dx;
		int currColumn = piece.getCurrentColumn() + dy;
		int remainingSteps = allowedSteps - 1;
		while (remainingSteps >= 0 && currRow >= 0 && currRow < 8 && currColumn >= 0 && currColumn < 8) {
			if (currRow == toRow && currColumn == toColumn)
				return true;
			if (game.getBoard()[currRow][currColumn] != null)
				break;
			currRow = currRow + dx;
			currColumn = currColumn + dy;
			remainingSteps--;
		}
		return false;
	}

	public boolean isLegal(boolean skipCheck) {
		return canReachEnd() && satisfiesConditions() && (skipCheck || !leavesCurrentPlayerInCheck());
	}

	private boolean leavesCurrentPlayerInCheck() {
		Game futureGame = game.clone();
		futureGame.makeMove(fromRow, fromColumn, toRow, toColumn, true);
		Color checkedColor = futureGame.colorInCheck();
		boolean leavesCurrentPlayerInCheck = checkedColor == piece.color;
		return leavesCurrentPlayerInCheck;
	}

	protected boolean satisfiesConditions() {
		return game.getBoard()[toRow][toColumn]==null || game.getBoard()[toRow][toColumn].color!= piece.color;
	}

	public void applyEffects(Game game) {
		for(MoveEffect effect : getEffects()){
			effect.apply(game);
		}
	}

	public int getFromRow() {
		return fromRow;
	}

	public int getFromColumn() {
		return fromColumn;
	}

}
