package models;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
	private static long currentId = 156;
	public static Map<Long, Game> games = new HashMap<Long, Game>();
	public static long newGame(String whitePlayer, String blackPlayer){
		games.put(currentId, new Game(currentId,whitePlayer, blackPlayer));
		return currentId++;
	}
}
