package com.johnkuper.main;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.dbconnect.ConnectionProvider;
import com.johnkuper.parser.PaymentParser;

public class Main {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private static ConnectionProvider conHelper = new ConnectionProvider();

	public static void main(String[] args) {

		/*
		 * Connection conn = conHelper.getConnection(); try { Statement stmt =
		 * conn.createStatement(); ResultSet rs;
		 * 
		 * rs = stmt .executeQuery(
		 * "SELECT bank_account,TIN,company_name,company_address From legal_payers"
		 * ); while (rs.next()) { logger.debug(rs.getString(1));
		 * logger.debug(rs.getString(2)); logger.debug(rs.getString(3));
		 * logger.debug(rs.getString(4)); } conn.close();
		 * 
		 * } catch (SQLException ex) { logger.error("SQL error", ex); } finally
		 * { }
		 */

		/*
		 * Path dir = Paths.get("src/main/resources"); try { new
		 * WatchDir(dir).processEvents(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */

		/*
		 * Payments payments = jaxb.getPayments(); jaxb.objectToXML(payments);
		 */
		PaymentParser parser;
		try {
			parser = new PaymentParser();
			parser.run();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
