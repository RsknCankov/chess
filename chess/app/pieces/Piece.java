package pieces;

import java.util.List;

import models.Color;
import models.Game;

public abstract class Piece {
	protected Color color;
	private Game game;
	protected int currentRow;
	protected int currentColumn;

	public Piece(Game game, Color color, int currRow, int currColumn) {
		this.color = color;
		this.setGame(game);
		this.setCurrentColumn(currColumn);
		this.setCurrentRow(currRow);
	}

	public Color getColor() {
		return color;
	}
	public abstract Piece clone();

	public boolean move(int toRow, int toColumn, boolean skipCheck) {
		for(Move move : getPossibleMoves(toRow, toColumn)){
			boolean legal = move.isLegal(skipCheck);
			if(legal){
				move.applyEffects(game);
				getGame().addMoveToHistory(move);
				return true;
			}
		}
		return false;
	}
	public boolean canMove(int toRow, int toColumn, boolean skipCheck){
		boolean can = false;
		for(Move move : getPossibleMoves(toRow, toColumn)){
			can = can || move.isLegal(skipCheck);
		}
		return can;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	public abstract int getId();
}
