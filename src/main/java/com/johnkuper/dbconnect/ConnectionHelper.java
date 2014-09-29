package com.johnkuper.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHelper {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");

	public Connection getConnection() {

		Connection connection = null;
		String driverName = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driverName);

			String serverName = "localhost";
			String mydatabase = "payments";
			String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

			String username = "root";
			String password = "root";

			connection = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException e) {
			logger.error("Class {} NotFound {}", driverName, e);
		} catch (SQLException e) {
			logger.error("SQLException during database connection", e);
		}

		return connection;

	}
}