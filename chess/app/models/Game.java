package models;

import java.util.ArrayList;
import java.util.List;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Move;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Game {
	private long id;
	private Piece[][] board = new Piece[8][8];
	private Color currentTurnColor = Color.WHITE;
	private List<Move> movesHistory = new ArrayList<>();

	public Game() {
		placeFirstRow(Color.WHITE);
		placeFirstRow(Color.BLACK);
		placePawnsRows(Color.WHITE);
		placePawnsRows(Color.BLACK);
	}

	private void placeFirstRow(Color color) {
		placePiece(new Rook(this, color, color.getFirstRow(), 1));
		placePiece(new Knight(this, color, color.getFirstRow(), 1));
		placePiece(new Bishop(this, color, color.getFirstRow(), 2));
		placePiece(new Queen(this, color, color.getFirstRow(), 3));
		placePiece(new King(this, color, color.getFirstRow(), 4));
		placePiece(new Bishop(this, color, color.getFirstRow(), 5));
		placePiece(new Knight(this, color, color.getFirstRow(), 6));
		placePiece(new Rook(this, color, color.getFirstRow(), 7));
	}

	private void placePawnsRows(Color color) {
		for (int i = 0; i < 8; ++i) {
			placePiece(new Pawn(this, color, color.getSecondRow(), i));
		}
	}

	private void placePiece(Piece piece) {
		getBoard()[piece.getCurrentRow()][piece.getCurrentColumn()] = piece;
	}

	public boolean makeMove(int fromRow, int fromColumn, int toRow, int toColumn) {
		Piece piece = getBoard()[fromRow][fromColumn];
		if (piece == null || piece.getColor() != currentTurnColor)
			return false;
		boolean moved = piece.move(toRow, toColumn);
		return moved;
	}

	public void addMoveToHistory(Move move) {
		movesHistory.add(move);
	}

	public Piece[][] getBoard() {
		return board;
	}

	public List<Move> getMovesHistory() {
		return movesHistory;
	}
}
