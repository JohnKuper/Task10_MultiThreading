package com.johnkuper.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.database.ComboPooledDataSourceProvider;
import com.johnkuper.manager.ThreadsManager;

public class Main {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");

	public static void main(String[] args) {
		
		XMLGenerator generator = new XMLGenerator();
		generator.generate(10, 10);

		ComboPooledDataSourceProvider poolProvider = new ComboPooledDataSourceProvider();
		ExecutorService executor = Executors.newCachedThreadPool();
		ThreadsManager thrManager = new ThreadsManager(poolProvider, executor);

		thrManager.startAllTasks();

		

	}

}
