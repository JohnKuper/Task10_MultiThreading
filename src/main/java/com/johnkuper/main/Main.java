package com.johnkuper.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.dbconnect.ConnectionHelper;

public class Main {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private static ConnectionHelper conHelper = new ConnectionHelper();

	public static void main(String[] args) {

		Connection conn = conHelper.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("SELECT bank_account,TIN,company_name,company_address From legal_payers");
			while (rs.next()) {
				logger.debug(rs.getString(1));
				logger.debug(rs.getString(2));
				logger.debug(rs.getString(3));
				logger.debug(rs.getString(4));
			}
			conn.close();

		} catch (SQLException ex) {
			logger.error("SQL error", ex);
		} finally {
		}

	}
}
