package com.johnkuper.model;

public class PhysicalPayer extends Physical {
	
	public String toString() {
		return  "Physical payer INFO: bank_account = " + bankAccount +
				" name = " + name +
				"; surname = " + surname +
				"; patronymic = " + patronymic +
				"; passport_series = " + passportSeries + 
				"; passportNumber = " + passportNumber +
				"; phone = " + phone;
	}

}
