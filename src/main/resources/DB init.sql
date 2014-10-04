create database if not exists DBpayments;

use DBpayments;

create table if not exists physical_payers (
	id int(10) auto_increment primary key,
	bank_account varchar (20) not null,
	name varchar(100),
	surname varchar(100),
	patronymic varchar(100),
	passport_series varchar(20),
	passport_number varchar(20),
	phone varchar(20),
	unique key unique_account (bank_account)
) engine=InnoDB;

create table if not exists physical_recipients LIKE physical_payers;

create table if not exists legal_payers (
	id int(10) auto_increment primary key,
	bank_account varchar (20) not null,
	TIN varchar(20),
	company_name varchar(100),
	company_address varchar(100),
	unique key unique_account (bank_account)
) engine=InnoDB;

create table if not exists legal_recipients LIKE legal_payers;

create table if not exists payment_details (
	id int(10) auto_increment primary key,
	bank_BIC varchar(30) not null,
	bank_name varchar(50),
	total decimal(10,2),
	date_of_payment date,
	date_of_execution date
) engine=InnoDB;

create table if not exists payments (
	id int(20) auto_increment primary key,
	id_physical_payer int (10),
	id_legal_payer int(10),
	id_physical_recipient int (10),
	id_legal_recipient int(10),
	id_detail int(10),
	payment_code varchar(20),
	unique key unique_code (payment_code),
	foreign key	fk_phys_payer(id_physical_payer) references physical_payers(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_phys_recipient(id_physical_recipient) references physical_recipients(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_legal_payer(id_legal_payer) references legal_payers(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_legal_recipient(id_legal_recipient) references legal_recipients(id)
	on delete set NULL
	on update cascade,
	foreign key	fk_detail(id_detail) references payment_details (id)
	on delete set NULL
	on update cascade
) engine=InnoDB;
	
	

