package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import graphic.Object;
import objects.GameObject;

public class SQLManager {
	static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
	static String DB_SUBURL = null;
	static String DB_URL = DB_SUBURL + "Highscore";
	static String USER = null;
	static String PASS = null;
	File oF;
	BufferedReader oR;
	String path;
	Game game;
	private int check=0;
	
	public SQLManager(String path, Game game) {
		this.path = path;
		this.game = game;
	}
	
	public void loadSQL() throws ClassNotFoundException, SQLException {
	Connection conn = null;
	Statement stmt = null;
	try {
		oF = new File(path);
		oR = new BufferedReader(new FileReader(oF));
		String line = oR.readLine();
		DB_SUBURL = line;
		String line2 = oR.readLine();
		USER = line2;
		String line3 = oR.readLine();
		if(line3 == null) PASS = "";
		else PASS = line3;
		oR.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
	System.out.println("STEP 1: Register JDBC driver");
	Class.forName(DRIVER_CLASS);

	System.out.println("STEP 2: Open a connection");
	conn = DriverManager.getConnection(DB_URL, USER, PASS);

	System.out.println("STEP 3: Execute a query");
	stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT * FROM Highscore");
	System.out.println("STEP 4: Extract data from result set");
	while (rs.next()) 
	{	
	game.setBestScore(rs.getInt(1));
	check = rs.getInt(1);
	game.setDate(rs.getDate(2));
	System.out.print("Highscore: " + rs.getInt(1) + "  " + rs.getDate(2));
	System.out.println("\n");
	}
	rs.close();
	}
	catch (SQLException e) {
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
	finally {
	System.out.println("STEP 5: Close connection");
	if (stmt != null)
	stmt.close();
	if (conn != null)
	conn.close();
	} 
	System.out.println("Done!");
	}
	
	public void saveSQL()throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		int highScore = game.getBestScore();
		try {
		System.out.println("STEP 1: Register JDBC driver");
		Class.forName(DRIVER_CLASS);

		System.out.println("STEP 2: Open a connection");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		System.out.println("STEP 3: save data to sql");
		if(check > game.getScore()) return;
		stmt = conn.createStatement();
		stmt.execute("DELETE FROM highscore");
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO highscore(Highscore , Time) VALUES (?,?)");
		pstmt.setInt(1, highScore);
		pstmt.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
		pstmt.executeUpdate();
		pstmt.close();
		} finally {
		System.out.println("STEP 4: Close connection");
		if (stmt != null)
		stmt.close();
		if (conn != null)
		conn.close();
		} 
		System.out.println("Done!");
	}
}
