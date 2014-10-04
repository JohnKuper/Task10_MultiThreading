package com.johnkuper.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.database.ConnectionProvider;
import com.johnkuper.manager.ThreadsManager;

public class Main {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private static ConnectionProvider provider = new ConnectionProvider();
	private static ThreadsManager thrManager = new ThreadsManager(provider);

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
		 * Payments payments = jaxb.getPayments(); jaxb.objectToXML(payments);
		 */
		/*
		 * PaymentParser parser; try { parser = new PaymentParser();
		 * parser.run(); } catch (JAXBException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		thrManager.startAllTasks();

	}

}
