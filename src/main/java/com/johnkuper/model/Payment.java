package com.johnkuper.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Payment", propOrder = { "physicalPayer", "physicalRecipient",
		"legalPayer", "legalRecipient", "details" })
public class Payment {

	@XmlElement(name = "physical_payer")
	protected Physical physicalPayer;
	@XmlElement(name = "physical_recipient")
	protected Physical physicalRecipient;
	@XmlElement(name = "legal_payer")
	protected Legal legalPayer;
	@XmlElement(name = "legal_recipient")
	protected Legal legalRecipient;
	protected Details details;

	public Physical getPhysicalPayer() {
		return physicalPayer;
	}

	public void setPhysicalPayer(Physical value) {
		this.physicalPayer = value;
	}

	public Physical getPhysicalRecipient() {
		return physicalRecipient;
	}

	public void setPhysicalRecipient(Physical value) {
		this.physicalRecipient = value;
	}

	public Legal getLegalPayer() {
		return legalPayer;
	}

	public void setLegalPayer(Legal value) {
		this.legalPayer = value;
	}

	public Legal getLegalRecipient() {
		return legalRecipient;
	}

	public void setLegalRecipient(Legal value) {
		this.legalRecipient = value;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details value) {
		this.details = value;
	}

}
