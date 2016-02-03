package pieces;

import java.util.List;

import models.Color;
import models.Game;

public abstract class Piece {
	protected Color color;
	protected Game game;
	protected int currentRow;
	protected int currentColumn;

	public Piece(Game game, Color color, int currRow, int currColumn) {
		this.color = color;
		this.game = game;
		this.setCurrentColumn(currColumn);
		this.setCurrentRow(currRow);
	}

	public Color getColor() {
		return color;
	}

	public boolean move(int toRow, int toColumn) {
		for(Move move : getPossibleMoves(toRow, toColumn)){
			boolean legal = move.isLegal();
			if(legal){
				move.applyEffects();
				game.addMoveToHistory(move);
				return true;
			}
		}
		return false;
	}

	

	public int getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	public int getCurrentColumn() {
		return currentColumn;
	}

	public void setCurrentColumn(int currentColumn) {
		this.currentColumn = currentColumn;
	}
	public abstract List<Move> getPossibleMoves(int toRow, int toColumn);
}
