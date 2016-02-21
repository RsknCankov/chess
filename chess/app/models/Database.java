package models;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	public static Connection getConnection() {
		try {
			// Heroku credentials

			String username = "lktiynoaqxxbtq";
			String password = "SwuRqwzxmSIFCalPx4h4UGfPq7";
			String dbUrl = "jdbc:postgresql://ec2-54-83-29-133.compute-1.amazonaws.com:5432/d8jkrnoohrqusm";

			// Local credentials
			/*
			 * String username = "postgres"; String password = "123456"; String
			 * dbUrl = "jdbc:postgresql://localhost:5432/mydb";
			 */
			return DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* Checks if username and password combination is valid */
	public static boolean credentialsValid(String username, String password) {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery(
					"SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + password + "'");
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Tries to create a new account with the given username and password and
	 * returns whether it was successful
	 */
	public static boolean register(String username, String password) {
		try {
			boolean usernameExists = usernameExists(username);
			if (!usernameExists) {
				Statement st = getConnection().createStatement();
				int executeUpdate = st.executeUpdate("INSERT INTO Users (ID, username, password) VALUES (DEFAULT, '"
						+ username + "', '" + password + "')");
				return executeUpdate > 0;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean usernameExists(String username) {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs;
			rs = statement.executeQuery("SELECT * FROM Users WHERE username = '" + username + "'");
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/* Creates a new game between the given players */
	public static long createGame(String whitePlayer, String blackPlayer) {
		try {
			Statement statement = getConnection().createStatement();
			statement
					.executeUpdate("INSERT INTO Games VALUES (DEFAULT, '" + whitePlayer + "', '" + blackPlayer + "');");
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	static {
		init();
	}

	/* Creates initial tables */
	public static void init() {
		try {
			Statement stmt = getConnection().createStatement();
			int executeUpdate = stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS Users(ID SERIAL,username varchar(255) NOT NULL,password varchar(255) NOT NULL,PRIMARY KEY (ID))");
			int executeUpdate2 = stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS Games(ID SERIAL, white varchar(255) NOT NULL, black varchar(255) NOT NULL, PRIMARY KEY (ID))");

			int executeUpdate3 = stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS Moves(ID SERIAL, GAME_ID int NOT NULL, fromRow int, fromColumn int, toRow int, toColumn int, timestamp TIMESTAMP, PRIMARY KEY (ID), FOREIGN KEY (GAME_ID) REFERENCES Games(ID))");

			System.out.println(executeUpdate + " " + executeUpdate2 + " " + executeUpdate3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
