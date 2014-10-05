package com.johnkuper.database;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ComboPooledDataSourceProvider implements IConnectionProvider {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	ComboPooledDataSource cpds;

	public ComboPooledDataSourceProvider() {
		cpds = new ComboPooledDataSource();
		setup();
	}

	private void setup() {

		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			logger.error("Error during 'setup' PooledProvider", e);
		}

		String serverName = "localhost";
		String mydatabase = "DBpayments";
		String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
		cpds.setJdbcUrl(url);

		String username = "root";
		cpds.setUser(username);

		String password = "root";
		cpds.setPassword(password);

		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);

	}

	@Override
	public Connection getConnection() {

		Connection conn = null;
		try {
			conn = cpds.getConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		} catch (SQLException e) {
			logger.error("Error during 'getConnection' in PooledProvider", e);
		}

		return conn;

	}

}
