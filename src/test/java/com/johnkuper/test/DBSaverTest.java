package com.johnkuper.test;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.johnkuper.database.ComboPooledDataSourceProvider;
import com.johnkuper.database.DBSaver;
import com.johnkuper.model.Legal;
import com.johnkuper.model.LegalPayer;
import com.johnkuper.model.LegalRecipient;
import com.johnkuper.model.Payment;
import com.johnkuper.model.Person;
import com.johnkuper.model.Physical;
import com.johnkuper.model.PhysicalPayer;
import com.johnkuper.model.PhysicalRecipient;
import com.johnkuper.storage.Storage;

@RunWith(MockitoJUnitRunner.class)
public class DBSaverTest {

	private Storage<Payment> paymentStorage;

	private ComboPooledDataSourceProvider provider;

	private void fillPhysical(Physical person) {
		person.setBankAccount("physical");
		person.setName("physical");
		person.setPassportNumber("physical");
		person.setPassportSeries("physical");
		person.setPatronymic("physical");
		person.setPhone("physical");
		person.setSurname("physical");
	}

	private void fillLegal(Legal person) {
		person.setBankAccount("legal");
		person.setCompanyAddress("legal");
		person.setCompanyName("legal");
		person.setTIN("legal");
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(DBSaverTest.this);
		paymentStorage = new Storage<>(5);
		provider = new ComboPooledDataSourceProvider();
	}

	@After
	public void cleanUp() {
		paymentStorage = null;
		provider = null;
	}

	@Test
	public void persistPayerOfPhysicalPayerShouldReturnLastIdArray()
			throws SQLException {

		DBSaver saver = new DBSaver(paymentStorage, provider);

		PhysicalPayer payer = new PhysicalPayer();
		fillPhysical(payer);

		Payment payment = new Payment();
		payment.setPayer(payer);

		String[] lastID = saver.persistPayer(payment);
		Assert.assertNotNull(lastID);
	}

	@Test
	public void persistPayerOfLegalPayerShouldReturnLastIdArray()
			throws SQLException {

		DBSaver saver = new DBSaver(paymentStorage, provider);

		LegalPayer payer = new LegalPayer();
		fillLegal(payer);

		Payment payment = new Payment();
		payment.setPayer(payer);

		String[] lastID = saver.persistPayer(payment);
		Assert.assertNotNull(lastID);
	}

	@Test
	public void persistPayerOfWrongPOJOShouldReturnNull() throws SQLException {

		class WrongPOJO extends Person {
		}

		DBSaver saver = new DBSaver(paymentStorage, provider);

		WrongPOJO pojo = new WrongPOJO();

		Payment payment = new Payment();
		payment.setPayer(pojo);

		String[] lastID = saver.persistPayer(payment);
		Assert.assertNull(lastID);
	}

	@Test
	public void persistRecipientOfPhysicalRecipientShouldReturnLastIdArray()
			throws SQLException {

		DBSaver saver = new DBSaver(paymentStorage, provider);

		PhysicalRecipient recipient = new PhysicalRecipient();
		fillPhysical(recipient);

		Payment payment = new Payment();
		payment.setRecipient(recipient);

		String[] lastID = saver.persistRecipient(payment);
		Assert.assertNotNull(lastID);
	}

	@Test
	public void persistRecipientOfLegalRecipientShouldReturnLastIdArray()
			throws SQLException {

		DBSaver saver = new DBSaver(paymentStorage, provider);

		LegalRecipient recipient = new LegalRecipient();
		fillLegal(recipient);

		Payment payment = new Payment();
		payment.setRecipient(recipient);

		String[] lastID = saver.persistRecipient(payment);
		Assert.assertNotNull(lastID);
	}

	@Test
	public void persistRecipientOfWrongPOJOShouldReturnNull()
			throws SQLException {

		class WrongPOJO extends Person {
		}

		DBSaver saver = new DBSaver(paymentStorage, provider);

		WrongPOJO pojo = new WrongPOJO();

		Payment payment = new Payment();
		payment.setRecipient(pojo);

		String[] lastID = saver.persistRecipient(payment);
		Assert.assertNull(lastID);
	}

}
