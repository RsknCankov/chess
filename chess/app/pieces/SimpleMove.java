package pieces;

import java.util.ArrayList;
import java.util.List;

import models.Game;

public class SimpleMove extends Move {

	public SimpleMove(int dx, int dy, int toRow, int toColumn, int allowedSteps, Piece piece) {
		super(dx, dy, toRow, toColumn, allowedSteps, piece);
	}

	@Override
	public List<MoveEffect> getEffects() {
		List<MoveEffect> effects = new ArrayList<>();
		effects.add(new MoveEffect() {
			@Override
			public void apply() {
				game.getBoard()[piece.getCurrentRow()][piece.getCurrentColumn()] = null;
				game.getBoard()[toRow][toColumn] = piece;
				piece.setCurrentRow(toRow);
				piece.setCurrentColumn(toColumn);
			}
		});
		return effects;
	}

}
