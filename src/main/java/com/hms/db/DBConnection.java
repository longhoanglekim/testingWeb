package com.hms.db;

import java.sql.Connection;
import java.sql.DriverManager;

//public class DBConnection {
//
//	private static Connection conn;
//
//	public static Connection getConn() {
//
//		try {
//
//			//step:1 for connection - load the driver class
//			Class.forName("com.mysql.cj.jdbc.Driver");
//
//			//step:2- create a connection
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dpapp","root","123456789");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//
//		return conn;
//	}
//}

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private static Connection connection = null;
	private final static String url = "jdbc:sqlite:data/dpapp.db";


	public String getUrl() {
		return url;
	}

	public DBConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			System.out.println("SQLite kết nối thành công");
		} catch (Exception e) {
			System.out.println("SQLite kết nối thất bại " + e.getMessage());
		}
	}

	public static Connection getConn() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			System.out.println("SQLite kết nối thành công");
		} catch (Exception e) {
			System.out.println("SQLite kết nối thất bại " + e.getMessage());
		}
		return connection;
	}

	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				System.out.println("THÀNH CÔNG: Đã đóng kết nối với CSDL");
			}
		} catch (Exception e) {
			System.out.println("LỖI: Lỗi đóng kết nối với CSDL");
		}
	}
}
