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

/* Class that represents a Game, the main interface of the application */
public class Game {
	private long id;
	private String whitePlayer;
	private String blackPlayer;
	private Piece[][] board = new Piece[8][8];
	private Color currentTurnColor = Color.WHITE;
	private List<Move> movesHistory = new ArrayList<>();
	private List<WebSocketHandler> connections = new ArrayList<>();

	public Game(long id, String whitePlayer, String blackPlayer) {
		this.setId(id);
		this.setWhitePlayer(whitePlayer);
		this.setBlackPlayer(blackPlayer);
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
		Game newGame = new Game(this.id, getWhitePlayer(), getBlackPlayer());
		Piece[][] newBoard = new Piece[8][8];
		for (int i = 0; i < newBoard.length; ++i) {
			for (int j = 0; j < newBoard[i].length; ++j) {
				if (board[i][j] != null) {
					newBoard[i][j] = board[i][j].clone();
					newBoard[i][j].setGame(newGame);
				}
			}
		}
		newGame.setId(id);
		newGame.board = newBoard;
		newGame.currentTurnColor = currentTurnColor;
		List<Move> newMoves = new ArrayList<>();
		newMoves.addAll(movesHistory);
		newGame.movesHistory = newMoves;
		return newGame;
	}

	/* Places the piece on the first row, for the given player color */
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

	/* Places all the pawn for the given color */
	private void placePawnsRows(Color color) {
		for (int i = 0; i < 8; ++i) {
			placePiece(new Pawn(this, color, color.getSecondRow(), i));
		}
	}

	private void placePiece(Piece piece) {
		getBoard()[piece.getCurrentRow()][piece.getCurrentColumn()] = piece;
	}

	/*
	 * Tries to make a move from the given positions and returns whether a move
	 * was made
	 */
	public boolean makeMove(int fromRow, int fromColumn, int toRow, int toColumn, boolean skipCheck) {
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

	/* Sends a message to all clients, subscribed to this game */
	private void sendMessage(String msg) {
		for (WebSocketHandler conn : connections) {
			conn.sendMessage(msg);
		}
	}

	/* Checks if the particular player can make any move for or is checkmated */
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

	/* Update the board state for all current connections */
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

	/*
	 * Tries all possible moves and checks if there is a possible move to
	 * capture the king
	 */
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

	public boolean usernameCanMakeMove(String username) {
		if (username == null)
			return false;
		return currentTurnColor == Color.WHITE && username.equals(getWhitePlayer())
				|| currentTurnColor == Color.BLACK && username.equals(getBlackPlayer());
	}

	public String getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public String getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(String blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
