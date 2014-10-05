package com.johnkuper.main;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGenerator {

	private static final String GENERATE_PATH = "src/main/resources/generateXML/file";
	final static Logger logger = LoggerFactory.getLogger("JohnKuper");

	public void generate(int files, int payments) {

		int x;
		int y;
		int total = 0;

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			for (x = 0; x < files; x++) {
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("PAYMENTS");
				doc.appendChild(rootElement);
				for (y = 0; y < payments; y++) {

					++total;

					String payer = "payer" + total;
					String recipient = "recipient" + total;
					String details = "details" + total;

					Element payment = doc.createElement("payment");
					rootElement.appendChild(payment);
					switch (y % 3) {
					case 0:
						addPhysicalElements(doc, payment, payer,
								"physical_payer");
						addPhysicalElements(doc, payment, recipient,
								"physical_recipient");
						addDetailsElements(doc, payment, details);
						break;
					case 1:
						addLegalElements(doc, payment, payer, "legal_payer");
						addLegalElements(doc, payment, recipient,
								"legal_recipient");
						addDetailsElements(doc, payment, details);
						break;
					case 2:
						addPhysicalElements(doc, payment, payer,
								"physical_payer");
						addLegalElements(doc, payment, recipient,
								"legal_recipient");
						addDetailsElements(doc, payment, details);
					}

				}

				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				String fileFullPath = GENERATE_PATH + x + ".xml";
				StreamResult result = new StreamResult(new File(fileFullPath));

				transformer.transform(source, result);

				logger.debug("File {} successfully generated!", fileFullPath);
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	private void addLegalElements(Document doc, Element payment, String payer,
			String tag) {

		Element legal = doc.createElement(tag);
		payment.appendChild(legal);

		Element bank_account = doc.createElement("bank_account");
		bank_account.appendChild(doc.createTextNode(payer));
		legal.appendChild(bank_account);

		Element TIN = doc.createElement("TIN");
		TIN.appendChild(doc.createTextNode(payer));
		legal.appendChild(TIN);

		Element company_name = doc.createElement("company_name");
		company_name.appendChild(doc.createTextNode(payer));
		legal.appendChild(company_name);

		Element company_address = doc.createElement("company_address");
		company_address.appendChild(doc.createTextNode(payer));
		legal.appendChild(company_address);

	}

	private void addPhysicalElements(Document doc, Element payment,
			String payer, String tag) {

		Element physical = doc.createElement(tag);
		payment.appendChild(physical);

		Element bank_account = doc.createElement("bank_account");
		bank_account.appendChild(doc.createTextNode(payer));
		physical.appendChild(bank_account);

		Element name = doc.createElement("name");
		name.appendChild(doc.createTextNode(payer));
		physical.appendChild(name);

		Element surname = doc.createElement("surname");
		surname.appendChild(doc.createTextNode(payer));
		physical.appendChild(surname);

		Element patronymic = doc.createElement("patronymic");
		patronymic.appendChild(doc.createTextNode(payer));
		physical.appendChild(patronymic);

		Element passport_series = doc.createElement("passport_series");
		passport_series.appendChild(doc.createTextNode(payer));
		physical.appendChild(passport_series);

		Element passport_number = doc.createElement("passport_number");
		passport_number.appendChild(doc.createTextNode(payer));
		physical.appendChild(passport_number);

		Element phone = doc.createElement("phone");
		phone.appendChild(doc.createTextNode(payer));
		physical.appendChild(phone);
	}

	private void addDetailsElements(Document doc, Element payment,
			String detailnode) {

		Element details = doc.createElement("details");
		payment.appendChild(details);

		Element bank_BIC = doc.createElement("bank_BIC");
		bank_BIC.appendChild(doc.createTextNode(detailnode));
		details.appendChild(bank_BIC);

		Element bank_name = doc.createElement("bank_name");
		bank_name.appendChild(doc.createTextNode(detailnode));
		details.appendChild(bank_name);

		Element total = doc.createElement("total");
		total.appendChild(doc.createTextNode("2500.50"));
		details.appendChild(total);

		Element date_of_payment = doc.createElement("date_of_payment");
		date_of_payment.appendChild(doc.createTextNode("2014-09-29"));
		details.appendChild(date_of_payment);

		Element date_of_execution = doc.createElement("date_of_execution");
		date_of_execution.appendChild(doc.createTextNode("2014-10-01"));
		details.appendChild(date_of_execution);

		Element payment_code = doc.createElement("payment_code");
		payment_code.appendChild(doc.createTextNode(detailnode));
		details.appendChild(payment_code);

	}

}
