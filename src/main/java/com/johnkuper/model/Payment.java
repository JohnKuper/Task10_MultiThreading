package com.johnkuper.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Payment", propOrder = { "payer", "recipient", "details" })
public class Payment {

	@XmlElements({
			@XmlElement(name = "physical_payer", type = PhysicalPayer.class),
			@XmlElement(name = "legal_payer", type = LegalPayer.class) })
	protected Person payer;
	@XmlElements({
			@XmlElement(name = "physical_recipient", type = PhysicalRecipient.class),
			@XmlElement(name = "legal_recipient", type = LegalRecipient.class) })
	protected Person recipient;

	protected Details details;

	public Person getPayer() {
		return payer;
	}

	public void setPayer(Person payer) {
		this.payer = payer;
	}

	public Person getRecipient() {
		return recipient;
	}

	public void setRecipient(Person recipient) {
		this.recipient = recipient;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details value) {
		this.details = value;
	}

}
