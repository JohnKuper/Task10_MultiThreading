package com.johnkuper.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.johnkuper.model.Payment;
import com.johnkuper.parser.PaymentParser;
import com.johnkuper.storage.Storage;

@RunWith(MockitoJUnitRunner.class)
public class PaymentParserTest {

	PaymentParser parser;

	Storage<Payment> paymentStorage;

	@Mock
	private Storage<Payment> paymentStorageMock;

	@Mock
	private Payment payment;

	@Before
	public void setMocks() {
		MockitoAnnotations.initMocks(PaymentParserTest.this);
		paymentStorage = new Storage<>(5);
	}

	@After
	public void cleanUp() {

		paymentStorage = null;
	}

	@Test
	public void paymentStorageShouldBeEmptyWhenXMLWithoutPayments()
			throws JAXBException, FileNotFoundException, InterruptedException {

		FileInputStream stream = new FileInputStream(
				"src/test/resources/xml/empty.xml");
		PaymentParser parser = new PaymentParser(stream, paymentStorageMock);
		parser.run();
		verify(paymentStorageMock, times(0)).put(payment);
	}

	@Test
	public void paymentStorageShouldContainPaymentFromXML()
			throws JAXBException, FileNotFoundException, InterruptedException {

		FileInputStream stream = new FileInputStream(
				"src/test/resources/xml/1_payments.xml");
		PaymentParser parser = new PaymentParser(stream, paymentStorage);
		parser.run();
		Payment payment = paymentStorage.get();
		Assert.assertEquals(Payment.class, payment.getClass());
	}

	@Test(expected = XMLStreamException.class)
	public void parseShouldThrownStreamExceptionWhenXMLBadFormed()
			throws FileNotFoundException, JAXBException, XMLStreamException,
			InterruptedException {

		FileInputStream stream = new FileInputStream(
				"src/test/resources/xml/badFormed.xml");
		PaymentParser parser = new PaymentParser(stream, paymentStorage);
		parser.parse();

	}

}
