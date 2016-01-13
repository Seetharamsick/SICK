package com.sick.dev.lib;

import java.sql.*;

public class JDBCConnector {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = null;
	String USER = null;
	String PASS = null;

	public JDBCConnector(String dbUrl, String userName, String password) {
		this.DB_URL = dbUrl;
		this.USER = userName;
		this.PASS = password;
	}

	public Statement getJDBCCOnnector() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(this.DB_URL, this.USER,
					this.PASS);
			System.out.println("Connected database successfully...");
			stmt = conn.createStatement();
			return stmt;
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		return stmt;
	}
}