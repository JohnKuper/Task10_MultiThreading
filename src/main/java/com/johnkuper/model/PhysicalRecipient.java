package com.johnkuper.model;

public class PhysicalRecipient extends Physical {
	
	public String toString() {
		return  "Physical recipient INFO: bank_account = " + bankAccount +
				" name = " + name +
				"; surname = " + surname +
				"; patronymic = " + patronymic +
				"; passport_series = " + passportSeries + 
				"; passportNumber = " + passportNumber +
				"; phone = " + phone;
	}

}
