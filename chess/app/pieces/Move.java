package pieces;

import java.util.List;

import models.Game;

public abstract class Move {
	private int dx;
	private int dy;
	private int allowedSteps;
	protected Piece piece;
	protected int toRow;
	protected int toColumn;
	protected Game game;

	public Move(int dx, int dy, int toRow, int toColumn, int allowedSteps, Piece piece) {
		this.dx = dx;
		this.dy = dy;
		this.allowedSteps = allowedSteps;
		this.piece = piece;
		this.toRow = toRow;
		this.toColumn = toColumn;
		this.game = piece.game;
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

	public boolean isLegal() {
		return canReachEnd() && satisfiesConditions();
	}

	protected boolean satisfiesConditions() {
		return game.getBoard()[toRow][toColumn]==null || game.getBoard()[toRow][toColumn].color!= piece.color;
	}

	public void applyEffects() {
		for(MoveEffect effect : getEffects()){
			effect.apply();
		}
	}

}
