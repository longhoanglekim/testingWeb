package com.hms.dao;

//import static java.sql.DriverManager.getConnection;
//import static jdk.internal.net.http.common.Log.logError;

import com.hms.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hms.entity.User;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

	private Connection conn;

	public UserDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	public boolean userRegister(User user) {
		boolean success = false;
		DBConnection connector = new DBConnection(); // Initialize connector
		try (Connection connection = connector.getConn()) { // Use try-with-resources for auto-close
			// Create the table if it doesn't exist
			String createTable = "CREATE TABLE IF NOT EXISTS user_details (id INTEGER PRIMARY KEY AUTOINCREMENT, full_name TEXT, email TEXT UNIQUE, password TEXT)";
			try (Statement statement = connection.createStatement()) {
				statement.execute(createTable);
			}

			String sql = "INSERT INTO user_details(full_name, email, password) VALUES(?, ?, ?)";
			try (PreparedStatement insertStatement = connection.prepareStatement(sql)) {
				insertStatement.setString(1, user.getFullName());
				insertStatement.setString(2, user.getEmail());
				insertStatement.setString(3, user.getPassword());
				int rowsAffected = insertStatement.executeUpdate();
				success = rowsAffected > 0; // Check if a row was inserted
			}
		} catch (SQLException e) {
			// Use a proper logging framework here
			// e.printStackTrace(); // Remove in production
			System.out.println("LỖI new account: " + e.getMessage());
		}
		System.out.println("Đăng ký tài khoản thành công"+success);
		return success;
	}


// Implement getConnection() and closeConnection() using connection pooling

//		boolean f = false;
//
//		try {
//			// insert user in db
//			String sql = "insert into user_details(full_name, email, password) values(?,?,?)";
//
//			PreparedStatement pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, user.getFullName());
//			pstmt.setString(2, user.getEmail());
//			pstmt.setString(3, user.getPassword());
//
//			pstmt.executeUpdate();
//
//			f = true; // if query execute successfully then f becomes true otherwise false...
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}

//		return f;
//	}

	// when call loginUser() method, it checks that particular user available or
	// not?
	// if not available then return null user object.
	// and if particular user available then, create User object(i.e user) and fetch
	// all the data of that user from db
	// and return that specific users object.
	public User loginUser(String email, String password) {
		User user = null;
		DBConnection connector = new DBConnection();
		try (Connection connection = connector.getConn()) {
			String sql = "SELECT * FROM user_details WHERE email = ? AND password = ?";
			try (PreparedStatement selectStatement = connection.prepareStatement(sql)) {
				selectStatement.setString(1, email);
				selectStatement.setString(2, password);
				ResultSet resultSet = selectStatement.executeQuery();

				if (resultSet.next()) {
					// Extract user information from the result set
					int id = resultSet.getInt("id");
					String fullName = resultSet.getString("full_name");
					String userEmail = resultSet.getString("email");
					String userPassword = resultSet.getString("password");
					// Assuming password is not retrieved for security reasons
					user = new User(id, fullName, userEmail, userPassword);
				}
			}
		} catch (SQLException e) {
			// Use a proper logging framework here
			System.out.println("LỖI login: " + e.getMessage());
		}
		return user;
	}


	//check old password
	public boolean checkOldPassword(int userId, String oldPassword) {

		boolean f = false;

		try {

			String sql = "select * from user_details where id=? and password=?";
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, oldPassword);

			ResultSet resultSet = pstmt.executeQuery();
			//System.out.println(resultSet);
			while (resultSet.next()) {
				f = true;
			}
		

		} catch (Exception e) {
			e.printStackTrace();
		}

		return f;
	}

	//change password
	public boolean changePassword(int userId, String newPassword) {

		boolean f = false;

		try {

			String sql = "update user_details set password=? where id=?";
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, newPassword);
			pstmt.setInt(2, userId);

			pstmt.executeUpdate();

			f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return f;
	}

}
