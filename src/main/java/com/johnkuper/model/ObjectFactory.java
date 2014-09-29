package com.johnkuper.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName _PAYMENTS_QNAME = new QName(
			"http://www.w3schools.com/RedsDevils", "PAYMENTS");

	public ObjectFactory() {
	}

	public Payments createPayments() {
		return new Payments();
	}

	public Physical createPhysical() {
		return new Physical();
	}

	public Payment createPayment() {
		return new Payment();
	}

	public Details createDetails() {
		return new Details();
	}

	public Legal createLegal() {
		return new Legal();
	}

	@XmlElementDecl(namespace = "http://www.w3schools.com/RedsDevils", name = "PAYMENTS")
	public JAXBElement<Payments> createPAYMENTS(Payments value) {
		return new JAXBElement<Payments>(_PAYMENTS_QNAME, Payments.class, null,
				value);
	}

}
