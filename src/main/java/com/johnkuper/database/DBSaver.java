package com.johnkuper.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.model.Details;
import com.johnkuper.model.Legal;
import com.johnkuper.model.LegalPayer;
import com.johnkuper.model.LegalRecipient;
import com.johnkuper.model.Payment;
import com.johnkuper.model.Physical;
import com.johnkuper.model.PhysicalPayer;
import com.johnkuper.model.PhysicalRecipient;
import com.johnkuper.storage.Storage;

public class DBSaver implements Runnable {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");

	private Storage<Payment> paymentStorage;
	private IConnectionProvider provider;

	public DBSaver(Storage<Payment> storage, IConnectionProvider provider) {
		this.paymentStorage = storage;
		this.provider = provider;

	}

	@Override
	public void run() {
		logger.debug("Start 'DBSaver' thread");
		persistAll();
	}

	private void persistAll() {

		while (true) {
			try {
				Payment payment = paymentStorage.get();

				String[] lastPayerID = persistPayer(payment);
				String[] lastRecipientID = persistRecipient(payment);
				long lastDetailID = persistDetails(payment.getDetails());
				persistPayment(payment.getDetails(), lastPayerID,
						lastRecipientID, lastDetailID);

			} catch (InterruptedException e) {
				logger.error("PaymentDBSaver thread was interrupted", e);

			} catch (SQLException e) {
				logger.error("SQL error during 'persistPayment'", e);
			}
		}
	}

	private java.sql.Date convertToSQLDate(XMLGregorianCalendar calendar) {
		java.util.Date dt = calendar.toGregorianCalendar().getTime();
		java.sql.Date sqlDt = new java.sql.Date(dt.getTime());
		return sqlDt;
	}

	public String[] persistPayer(Payment payment) throws SQLException {

		String[] arrayLastID = { null, null };
		long lastPayerId;

		if (payment.getPayer() instanceof PhysicalPayer) {
			PhysicalPayer physPayer = (PhysicalPayer) payment.getPayer();
			lastPayerId = persistPhysical(physPayer, "physical_payers");
			arrayLastID[0] = String.valueOf(lastPayerId);
			return arrayLastID;
		} else if (payment.getPayer() instanceof LegalPayer) {
			LegalPayer legalPayer = (LegalPayer) payment.getPayer();
			lastPayerId = persistLegal(legalPayer, "legal_payers");
			arrayLastID[1] = String.valueOf(lastPayerId);
			return arrayLastID;
		}

		return null;

	}

	public String[] persistRecipient(Payment payment) throws SQLException {

		String[] arrayLastID = { null, null };
		long lastRecipientId;

		if (payment.getRecipient() instanceof PhysicalRecipient) {
			PhysicalRecipient physRecipient = (PhysicalRecipient) payment
					.getRecipient();
			lastRecipientId = persistPhysical(physRecipient,
					"physical_recipients");
			arrayLastID[0] = String.valueOf(lastRecipientId);
			return arrayLastID;
		} else if (payment.getRecipient() instanceof LegalRecipient) {
			LegalRecipient legalRecipient = (LegalRecipient) payment
					.getRecipient();
			lastRecipientId = persistLegal(legalRecipient, "legal_recipients");
			arrayLastID[1] = String.valueOf(lastRecipientId);
			return arrayLastID;
		}
		return null;
	}

	private void persistPayment(Details details, String[] lastPayerID,
			String[] lastRecipientID, long lastDetailID) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO payments"
				+ "(id_physical_payer,id_legal_payer,id_physical_recipient,id_legal_recipient,id_detail,payment_code) VALUES"
				+ "(?,?,?,?,?,?)";

		try {
			connection = provider.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);

			preparedStatement.setString(1, lastPayerID[0]);
			preparedStatement.setString(2, lastPayerID[1]);
			preparedStatement.setString(3, lastRecipientID[0]);
			preparedStatement.setString(4, lastRecipientID[1]);
			preparedStatement.setString(5, String.valueOf(lastDetailID));
			preparedStatement.setString(6, details.getPaymentCode());

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			if (e.getSQLState().equals(IConnectionProvider.DUPLICATE_STATE)) {
				logger.warn("Duplicate entry. Keep working");
				return;
			} else {
				logger.error("SQL error during 'persistDetails'", e);
				throw e;

			}

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connection != null) {
				connection.close();
			}

		}
	}

	private long persistDetails(Details details) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO payment_details"
				+ "(bank_BIC,bank_name,total,date_of_payment,date_of_execution) VALUES"
				+ "(?,?,?,?,?)";

		try {

			connection = provider.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL,
					Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, details.getBankBIC());
			preparedStatement.setString(2, details.getBankName());
			preparedStatement.setBigDecimal(3, details.getTotal());

			Date dateOfPayment = convertToSQLDate(details.getDateOfPayment());
			preparedStatement.setDate(4, dateOfPayment);

			Date dateOfExecution = convertToSQLDate(details
					.getDateOfExecution());
			preparedStatement.setDate(5, dateOfExecution);

			preparedStatement.executeUpdate();

			long lastInsertID = getLastInsertId(preparedStatement);
			return lastInsertID;

		} catch (SQLException e) {
			if (e.getSQLState().equals(IConnectionProvider.DUPLICATE_STATE)) {
				logger.warn("Duplicate entry. Keep working");
				return -1;
			} else {
				logger.error("SQL error during 'persistDetails'", e);
				throw e;

			}
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connection != null) {
				connection.close();
			}

		}

	}

	private long getLastInsertId(PreparedStatement statement)
			throws SQLException {

		ResultSet generatedKeys = statement.getGeneratedKeys();

		if (generatedKeys.next()) {
			return generatedKeys.getLong(1);

		} else {
			throw new SQLException("Creating failed. No ID obtained.");
		}

	}

	private long persistLegal(Legal legal, String table) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + table
				+ "(bank_account,TIN,company_name,company_address) VALUES"
				+ "(?,?,?,?)";

		try {
			connection = provider.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL,
					Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, legal.getBankAccount());
			preparedStatement.setString(2, legal.getTIN());
			preparedStatement.setString(3, legal.getCompanyName());
			preparedStatement.setString(4, legal.getCompanyAddress());

			preparedStatement.executeUpdate();

			long lastInsertID = getLastInsertId(preparedStatement);
			return lastInsertID;

		} catch (SQLException e) {
			if (e.getSQLState().equals(IConnectionProvider.DUPLICATE_STATE)) {
				logger.warn("Duplicate entry. Keep working");
				return -1;
			} else {
				logger.error("SQL error during 'persistDetails'", e);
				throw e;

			}
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connection != null) {
				connection.close();
			}

		}
	}

	private long persistPhysical(Physical physical, String table)
			throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO "
				+ table
				+ "(bank_account,name,surname,patronymic,passport_series,passport_number,phone) VALUES"
				+ "(?,?,?,?,?,?,?)";

		try {
			connection = provider.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL,
					Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, physical.getBankAccount());
			preparedStatement.setString(2, physical.getName());
			preparedStatement.setString(3, physical.getSurname());
			preparedStatement.setString(4, physical.getPatronymic());
			preparedStatement.setString(5, physical.getPassportSeries());
			preparedStatement.setString(6, physical.getPassportNumber());
			preparedStatement.setString(7, physical.getPhone());

			preparedStatement.executeUpdate();

			long lastInsertID = getLastInsertId(preparedStatement);
			return lastInsertID;

		} catch (SQLException e) {
			if (e.getSQLState().equals(IConnectionProvider.DUPLICATE_STATE)) {
				logger.warn("Duplicate entry. Keep working");
				return -1;
			} else {
				logger.error("SQL error during 'persistDetails'", e);
				throw e;

			}
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connection != null) {
				connection.close();
			}

		}

	}
}
