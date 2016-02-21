package pieces;

import models.Game;
/* Class that represents an effect of a move, for example moving of pieces or capturing*/
public interface MoveEffect {
	void apply(Game game);
}
