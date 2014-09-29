package com.johnkuper.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Physical", propOrder = { "bankAccount", "name", "surname",
		"patronymic", "passportSeries", "passportNumber", "phone" })
public class Physical {

	@XmlElement(name = "bank_account", required = true)
	protected String bankAccount;
	@XmlElement(required = true)
	protected String name;
	@XmlElement(required = true)
	protected String surname;
	@XmlElement(required = true)
	protected String patronymic;
	@XmlElement(name = "passport_series", required = true)
	protected String passportSeries;
	@XmlElement(name = "passport_number", required = true)
	protected String passportNumber;
	@XmlElement(required = true)
	protected String phone;

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String value) {
		this.bankAccount = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String value) {
		this.surname = value;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String value) {
		this.patronymic = value;
	}

	public String getPassportSeries() {
		return passportSeries;
	}

	public void setPassportSeries(String value) {
		this.passportSeries = value;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String value) {
		this.passportNumber = value;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String value) {
		this.phone = value;
	}

}
