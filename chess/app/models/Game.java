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
	private List<WebSocketHandler> connections = new ArrayList<>();

	public Game() {
		placeFirstRow(Color.WHITE);
		placeFirstRow(Color.BLACK);
		placePawnsRows(Color.WHITE);
		placePawnsRows(Color.BLACK);
	}

	public void addConnection(WebSocketHandler connection) {
		connections.add(connection);
	}

	public void removeConnection(WebSocketHandler connection) {
		connections.remove(connection);
	}

	public Game clone() {
		Game newGame = new Game();
		Piece[][] newBoard = new Piece[8][8];
		for (int i = 0; i < newBoard.length; ++i) {
			for (int j = 0; j < newBoard[i].length; ++j) {
				if (board[i][j] != null) {
					newBoard[i][j] = board[i][j].clone();
					newBoard[i][j].setGame(newGame);
				}
			}
		}
		newGame.id = id;
		newGame.board = newBoard;
		newGame.currentTurnColor = currentTurnColor;
		List<Move> newMoves = new ArrayList<>();
		newMoves.addAll(movesHistory);
		newGame.movesHistory = newMoves;
		return newGame;
	}

	private void placeFirstRow(Color color) {
		placePiece(new Rook(this, color, color.getFirstRow(), 0));
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

	public boolean makeMove(int fromRow, int fromColumn, int toRow, int toColumn, boolean skipCheck) {
		System.out.println("MAKE MOVE " + skipCheck);
		Piece piece = getBoard()[fromRow][fromColumn];
		if (piece == null || piece.getColor() != currentTurnColor)
			return false;
		Move move = piece.move(toRow, toColumn, skipCheck);
		if (move != null) {
			switchCurrentColor();
			refreshAllConnectionBoards();
			sendMessage(move.toString());
			Color color = colorInCheck();
			if (color != null) {
				if (skipCheck || canMakeMove(color)) {
					sendMessage("Check");
				} else {
					sendMessage("Checkmate");
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private void sendMessage(String msg) {
		for (WebSocketHandler conn : connections) {
			conn.sendMessage(msg);
		}
	}

	private boolean canMakeMove(Color color) {
		for (int fromRow = 0; fromRow < 8; ++fromRow) {
			for (int fromColumn = 0; fromColumn < 8; ++fromColumn) {
				for (int toRow = 0; toRow < 8; ++toRow) {
					for (int toColumn = 0; toColumn < 8; ++toColumn) {
						Piece piece = getBoard()[fromRow][fromColumn];
						if (piece != null && piece.getColor() == color && piece.canMove(toRow, toColumn, false)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void refreshAllConnectionBoards() {
		for (WebSocketHandler conn : connections) {
			conn.refreshBoard();
		}
	}

	private void switchCurrentColor() {
		if (currentTurnColor == Color.WHITE) {
			currentTurnColor = Color.BLACK;
		} else {
			currentTurnColor = Color.WHITE;
		}
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

	public boolean wasMoved(int x, int y) {
		for (Move move : movesHistory) {
			if (move.getFromRow() == x && move.getFromColumn() == y) {
				return true;
			}
		}
		return false;
	}

	public Color colorInCheck() {
		for (int fromRow = 0; fromRow < 8; ++fromRow) {
			for (int fromColumn = 0; fromColumn < 8; ++fromColumn) {
				for (int toRow = 0; toRow < 8; ++toRow) {
					for (int toColumn = 0; toColumn < 8; ++toColumn) {
						Piece fromPiece = getBoard()[fromRow][fromColumn];
						Piece toPiece = getBoard()[toRow][toColumn];
						if (fromPiece != null && toPiece != null && toPiece instanceof King
								&& fromPiece.canMove(toRow, toColumn, true)) {
							return toPiece.getColor();
						}
					}
				}
			}
		}
		return null;
	}
}
