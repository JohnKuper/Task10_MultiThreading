package com.johnkuper.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Payments", propOrder = { "payment" })
@XmlRootElement(name = "PAYMENTS")
public class Payments {

	protected List<Payment> payment;

	public List<Payment> getPayment() {
		if (payment == null) {
			payment = new ArrayList<Payment>();
		}
		return this.payment;
	}

}
