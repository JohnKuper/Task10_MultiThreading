package com.johnkuper.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.database.ConnectionProvider;
import com.johnkuper.database.PaymentDBSaver;
import com.johnkuper.model.Payment;
import com.johnkuper.parser.PaymentParser;
import com.johnkuper.remover.FileRemover;
import com.johnkuper.storage.Storage;
import com.johnkuper.watcher.WatchDir;

public class ThreadsManager {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private Storage<Path> pathStorage = new Storage<>(1000000);
	private Storage<Payment> paymentStorage = new Storage<>(1000000);
	private Storage<Future<Path>> deletePathStorage = new Storage<>(1000000);
	private ExecutorService executor = Executors.newFixedThreadPool(100);
	private ConnectionProvider provider;

	public ThreadsManager(ConnectionProvider provider) {
		this.provider = provider;
	}

	private void runWatcher() {
		logger.debug("Start 'runWatcher'");

		Path dir = Paths.get("src/main/resources/xml");
		try {
			executor.execute(new WatchDir(dir, pathStorage));
			/*
			 * long threadid = Thread.currentThread().getId();
			 * logger.debug("Thread's ID = {}", threadid);
			 */
		} catch (IOException e) {
			String msg = "Error during 'runWatcher'";
			logger.error(msg, e);
		}

	}

	public void startAllTasks() {
		runWatcher();
		runFileRemover();
		runSaver();
		// startFileDeletingTask(deletePathStorage);
		runParser();

	}

	private void runSaver() {
		logger.debug("Start 'runSaver'");
		int x;
		for (x = 0; x < 20; x++) {
			executor.submit(new PaymentDBSaver(paymentStorage, provider));
		}
	}

	private void runFileRemover() {
		logger.debug("Start 'runDelete'");
		executor.submit(new FileRemover(deletePathStorage));

	}

	private void runParser() {
		logger.debug("Start 'runParser'");
		Path currentFile = null;
		while (true) {
			try {
				currentFile = pathStorage.get();
				try {
					File fileForParsing = currentFile.toFile();
					logger.debug("Start parsing '{}' file", currentFile);
					FileInputStream stream = new FileInputStream(fileForParsing);
					Runnable parser = new PaymentParser(stream, paymentStorage);
					Future<Path> fileForDelete = executor.submit(parser,
							currentFile);
					logger.debug(
							"File '{}' was successfully parsed and add for delete",
							currentFile);
					deletePathStorage.put(fileForDelete);

				} catch (FileNotFoundException e) {
					logger.error(
							"File '{}' not found or can't be read. Put it back to pathStorage",
							e);
					pathStorage.put(currentFile);

				}
			} catch (InterruptedException e) {
				logger.debug("'ThreadsManager' thread was interrupted", e);
				return;
			} catch (JAXBException e) {
				logger.error("Error during parsing xml {} {}",
						currentFile.toAbsolutePath(), e.getMessage());
			}
		}
	}

	/*
	 * private void startFileDeletingTask(Storage<Future<Path>> taskStorage) {
	 * executor.submit(() -> { while (true) { Future<Path> task =
	 * taskStorage.get(); if (task.isDone()) {
	 * logger.debug("Start new 'fileDeletingTask'");
	 * logger.debug(String.format("Parsing %s complete", task.get()));
	 * Files.deleteIfExists(task.get()); } else taskStorage.put(task); } }); }
	 */

}
