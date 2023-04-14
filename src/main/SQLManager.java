package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class SQLManager {
	static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
	static String DB_SUBURL = null;
	static String DB_URL = null;
	static String USER = null;
	static String PASS = null;
	File oF;
	BufferedReader oR;
	String path;
	Game game;
	
	public SQLManager(String path, Game game) {
		this.path = path;
		this.game = game;
	}
	
	public void loadSQL() throws ClassNotFoundException, SQLException{
	Connection conn = null;
	Statement stmt = null;
	try {
		oF = new File(path);
		oR = new BufferedReader(new FileReader(oF));
		String line = oR.readLine();
		DB_SUBURL = line;
		DB_URL = DB_SUBURL + "Highscore";
		String line2 = oR.readLine();
		USER = line2;
		String line3 = oR.readLine();
		if(line3 == null) PASS = "";
		else PASS = line3;
		oR.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	try {
		Class.forName(DRIVER_CLASS);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Highscore");
		while (rs.next()) 
		{	
			game.setBestScore(rs.getInt(1));
			game.setDate(rs.getDate(2));
		}
		rs.close();
	}
	catch (SQLSyntaxErrorException e) {
		createNewDB();
	}
	finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} 
	}
	
	public void saveSQL()throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER_CLASS);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt.execute("DELETE FROM highscore");
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO highscore(Highscore , Time) VALUES (?,?)");
			pstmt.setInt(1, game.getBestScore());
			pstmt.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
			pstmt.executeUpdate();
			pstmt.close();
		} 
		finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} 
	}
	
	private void createNewDB() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		conn = DriverManager.getConnection(DB_SUBURL, USER, PASS);
		stmt = conn.createStatement();
		stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS highscore");
		DB_URL = DB_SUBURL + "Highscore";
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS Highscore " +
                   "(Highscore INTEGER, " +
                   " Time date" + 
                   	")"; 
		stmt.executeUpdate(sql);
	}
}
