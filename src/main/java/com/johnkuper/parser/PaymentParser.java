package com.johnkuper.parser;

import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.model.Payment;
import com.johnkuper.storage.Storage;

public class PaymentParser implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private JAXBContext jaxbcontext;
	private Unmarshaller unmarshaller;
	private FileInputStream stream;
	private Storage<Payment> paymentStorage;

	public PaymentParser(FileInputStream stream, Storage<Payment> storage)
			throws JAXBException {
		this.jaxbcontext = JAXBContext.newInstance(Payment.class);
		this.unmarshaller = jaxbcontext.createUnmarshaller();
		this.stream = stream;
		this.paymentStorage = storage;

	}

	public void run() {
		logger.debug("Start 'PaymentParser' thread");
		try {
			parse();
		} catch (XMLStreamException e) {
			String msg = ("Error during parsing XML");
			logger.error(msg, e);
		} catch (InterruptedException e) {
			String msg = ("'PaymentParser' thread was interrupted");
			logger.error(msg, e);
		}

	}

	private void parse() throws XMLStreamException, InterruptedException {

		XMLInputFactory xif = XMLInputFactory.newFactory();
		XMLEventReader xmlreader = null;

		try {

			xmlreader = xif.createXMLEventReader(stream);
			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.peek();
				if (event.isStartElement()
						&& event.asStartElement().getName().getLocalPart()
								.equals("payment")) {
					Payment payment = getPayment(xmlreader);
					paymentStorage.put(payment);
					/*
					 * logger.debug(payment.getPayer().toString());
					 * logger.debug(payment.getRecipient().toString());
					 * logger.debug(payment.getDetails().toString());
					 */
				} else {
					xmlreader.nextEvent();
				}
			}
		}

		finally {
			if (xmlreader != null) {
				xmlreader.close();
			}
		}

	}

	private Payment getPayment(XMLEventReader xmlreader) {
		try {
			JAXBElement<Payment> jb = unmarshaller.unmarshal(xmlreader,
					Payment.class);
			Payment payment = jb.getValue();
			return payment;
		} catch (JAXBException e) {
			logger.error("Error during parsing single payment from XML :", e);
		}
		return null;
	}
}
