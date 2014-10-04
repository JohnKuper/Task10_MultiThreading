package com.johnkuper.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Details", propOrder = { "bankBIC", "bankName", "total",
		"dateOfPayment", "dateOfExecution", "paymentCode" })
public class Details {

	@XmlElement(name = "bank_BIC", required = true)
	protected String bankBIC;
	@XmlElement(name = "bank_name", required = true)
	protected String bankName;
	@XmlElement(required = true)
	protected BigDecimal total;
	@XmlElement(name = "date_of_payment", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dateOfPayment;
	@XmlElement(name = "date_of_execution", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dateOfExecution;
	@XmlElement(name = "payment_code", required = true)
	protected String paymentCode;

	public String toString() {
		return String.format("Details INFO : bank_BIC = " + bankBIC
				+ "; bank_name = " + bankName + "; total = " + total
				+ "; date_of_payment = " + dateOfPayment
				+ "; date_of_execution = " + dateOfExecution
				+ "; payment_code = " + paymentCode + "%n");
	}

	public String getBankBIC() {
		return bankBIC;
	}

	public void setBankBIC(String value) {
		this.bankBIC = value;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String value) {
		this.bankName = value;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal value) {
		this.total = value;
	}

	public XMLGregorianCalendar getDateOfPayment() {
		return dateOfPayment;
	}

	public void setDateOfPayment(XMLGregorianCalendar value) {
		this.dateOfPayment = value;
	}

	public XMLGregorianCalendar getDateOfExecution() {
		return dateOfExecution;
	}

	public void setDateOfExecution(XMLGregorianCalendar value) {
		this.dateOfExecution = value;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String value) {
		this.paymentCode = value;
	}

}
