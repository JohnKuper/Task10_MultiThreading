package com.johnkuper.model;

public class LegalPayer extends Legal {
	
	public String toString() {
		return "Legal payer INFO: bank_account = " + bankAccount +
				" TIN = " + tin +
				"; company_name = " + companyName +
				"; company_address = " + companyAddress;
	}

}
