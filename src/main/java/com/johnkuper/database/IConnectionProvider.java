package com.johnkuper.database;

import java.sql.Connection;

public interface IConnectionProvider {

	Connection getConnection();

}
