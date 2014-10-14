package application;

import java.sql.*;

public class db_connect {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/della";

	static final String USER = "root";
	static final String PASS = "root";
	
	static Connection  conn = null;

	public static Connection connect() {

		

		try {

			Class.forName("com.mysql.jdbc.Driver");
			
			
			
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			
			
			
			
			System.out.println("Connecting to database...");

			

			if (conn.isClosed())
				conn = null;

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Cannot connect to the DB");
		}
		return conn;

	}
}
