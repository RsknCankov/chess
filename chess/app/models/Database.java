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
//	    URI dbUri = new URI(System.getenv("DATABASE_URL"));
	    /*
	    String username = "lktiynoaqxxbtq"; //dbUri.getUserInfo().split(":")[0];
	    String password =  "SwuRqwzxmSIFCalPx4h4UGfPq7";//dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
*/
	    String username = "postgres";
	    String password = "123456";
	    String dbUrl = "jdbc:postgresql://localhost:5432/mydb";
			return DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static boolean credentialsValid(String username, String password){
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + password + "'");
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean register(String username, String password){
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE username = '" + username + "'");
			boolean next = rs.next();
			if(!next){
				Statement st = getConnection().createStatement();
				int executeUpdate = st.executeUpdate("INSERT INTO Users (ID, username, password) VALUES (DEFAULT, '" + username + "', '" + password +"')");
				return executeUpdate>0;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	static {
        init();
	}
	public static void init() {
		try {
        	Statement stmt = getConnection().createStatement();
			int executeUpdate = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users(ID SERIAL,username varchar(255) NOT NULL,password varchar(255) NOT NULL,PRIMARY KEY (ID))");
			System.out.println(executeUpdate);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
