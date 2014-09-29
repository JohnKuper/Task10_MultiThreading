create database if not exists payments;

use payments;

create table if not exists physical_payers (
	id int(10) auto_increment primary key,
	bank_account varchar (20) not null,
	name varchar(100),
	surname varchar(100),
	patronymic varchar(100),
	passport_series char(4),
	passport_number char(6),
	phone varchar(20),
	unique key unique_account (bank_account)
);

create table if not exists physical_recipients LIKE physical_payers;

create table if not exists legal_payers (
	id int(10) auto_increment primary key,
	bank_account varchar (20) not null,
	TIN varchar(12),
	company_name varchar(100),
	company_address varchar(100),
	unique key unique_account (bank_account)
);

create table if not exists legal_recipients LIKE legal_payers;

create table if not exists payment_details (
	id int(10) auto_increment primary key,
	bank_BIC varchar(30) not null,
	bank_name varchar(50),
	total decimal(10,2),
	date_of_payment date,
	date_of_execution date
);

create table if not exists payment (
	id int(20) auto_increment primary key,
	id_physical_payers int (10),
	id_physical_recipients int (10),	
	id_legal_payers int(10),
	id_legal_recipients int(10),
	id_details int(10),
	payment_code varchar(20),
	unique key unique_code (payment_code),
	foreign key	fk_phys_payer(id_physical_payers) references physical_payers(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_phys_recipient(id_physical_recipients) references physical_recipients(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_legal_payer(id_legal_payers) references legal_payers(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_legal_recipient(id_legal_recipients) references legal_recipients(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_detail(id_details) references payment_details(id)
	on delete set NULL
	on update cascade
);
	
	

