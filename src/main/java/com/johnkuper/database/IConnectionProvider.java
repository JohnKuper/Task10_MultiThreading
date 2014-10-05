package com.johnkuper.database;

import java.sql.Connection;

public interface IConnectionProvider {
	
	public static final String DUPLICATE_STATE = "23000";

	Connection getConnection();

}
