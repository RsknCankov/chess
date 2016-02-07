package models;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
	public static Map<Long, Game> games = new HashMap<Long, Game>();
	static {
		games.put((long) 1, new Game());
	}
}
