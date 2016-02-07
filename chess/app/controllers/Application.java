package controllers;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Map;

import org.h2.command.dml.Call;

import models.Game;
import models.GameManager;
import models.Pinger;
import models.WebSocketHandler;
import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;
import views.html.game;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;

public class Application extends Controller {
	public WebSocket<String> socketWs(long gameId) {
		Game game = GameManager.games.get(gameId);
		if (game == null) {
			return WebSocket.reject(forbidden());
		}
		return new WebSocketHandler(game);
	}

	public Result socketJs(long gameId) {
		return ok(views.js.socket.render(gameId));
	}

	public Result index() {
		return ok("Index");
	}

	public Result game(long id) {
		return ok(game.render(id));
	}
}
