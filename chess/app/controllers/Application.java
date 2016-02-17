package controllers;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Map;

import org.h2.command.dml.Call;

import models.Database;
import models.Game;
import models.GameManager;
import models.UserForm;
import models.WebSocketHandler;
import play.data.Form;
import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;
import views.html.game;
import views.html.login;
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

	public Result login() {
		return ok(login.render());
	}
	public Result register(){
		return ok(views.html.register.render());
	}
	public Result registerPost() {
		Form<UserForm> form = Form.form(UserForm.class);
		UserForm uf = form.bindFromRequest().get();
		System.out.println(uf.username + " " + uf.password);
		boolean registered = Database.register(uf.username, uf.password);
		System.out.println("REGISTERED: " + registered);
		return redirect(controllers.routes.Application.index());
	}
	public Result loginPost(){
		Form<UserForm> form = Form.form(UserForm.class);
		UserForm uf = form.bindFromRequest().get();
		boolean valid = Database.credentialsValid(uf.username, uf.password);
		if(valid){
			session().clear();
			session("username", uf.username);
		} else {
			return unauthorized("Wrong username or password");
		}
		return redirect(controllers.routes.Application.index());
	}
	public Result index() {
		Database.getConnection();
		String username = session("username");
		if(username == null){
			username = "Guest";
		}
		return ok(views.html.index.render(username));
	}
	public Result logout(){
		session().clear();
		return redirect(controllers.routes.Application.index());
	}

	public Result game(long id) {
		return ok(game.render(id));
	}
}
