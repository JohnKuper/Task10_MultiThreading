package com.johnkuper.parser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.model.Payment;

public class PaymentParser implements Runnable {

	private static final String SOURCE_XML = "src/main/resources/payments.xml";
	private final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private JAXBContext jaxbcontext;
	private Unmarshaller unmarshaller;

	public PaymentParser() throws JAXBException {
		this.jaxbcontext = JAXBContext.newInstance(Payment.class);
		this.unmarshaller = jaxbcontext.createUnmarshaller();

	}

	public void run() {
		logger.debug("Start parsing");
		parse();
	}

	public void parse() {

		XMLInputFactory xif = XMLInputFactory.newFactory();
		StreamSource stream = new StreamSource(SOURCE_XML);
		XMLEventReader xmlreader = null;

		try {

			xmlreader = xif.createXMLEventReader(stream);
			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.peek();
				if (event.isStartElement()
						&& event.asStartElement().getName().getLocalPart()
								.equals("payment")) {
					Payment payment = getPayment(xmlreader);
					logger.debug(payment.getPayer().toString());
					logger.debug(payment.getRecipient().toString());
					logger.debug(payment.getDetails().toString());
				} else {
					xmlreader.nextEvent();
				}
			}
		} catch (XMLStreamException e) {
			String msg = ("Error during parsing XML");
			logger.error(msg, e);
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
