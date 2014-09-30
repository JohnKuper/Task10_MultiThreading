package com.johnkuper.model;

public class LegalRecipient extends Legal {

	public String toString() {
		return "Legal recipient INFO: bank_account = " + bankAccount
				+ " TIN = " + tin + "; company_name = " + companyName
				+ "; company_address = " + companyAddress;
	}

}
