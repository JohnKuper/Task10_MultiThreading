package com.johnkuper.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person", propOrder = "bankAccount")
public class Person {

	@XmlElement(name = "bank_account", required = true)
	protected String bankAccount;

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String toString() {
		return "bank_account = " + bankAccount;
	}
}
