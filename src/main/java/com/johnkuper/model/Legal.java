package com.johnkuper.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Legal", propOrder = { "bankAccount", "tin", "companyName",
		"companyAddress" })
public class Legal {

	@XmlElement(name = "bank_account", required = true)
	protected String bankAccount;
	@XmlElement(name = "TIN", required = true)
	protected String tin;
	@XmlElement(name = "company_name", required = true)
	protected String companyName;
	@XmlElement(name = "company_address", required = true)
	protected String companyAddress;

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String value) {
		this.bankAccount = value;
	}

	public String getTIN() {
		return tin;
	}

	public void setTIN(String value) {
		this.tin = value;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String value) {
		this.companyName = value;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String value) {
		this.companyAddress = value;
	}

}
