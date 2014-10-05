package com.johnkuper.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.database.IConnectionProvider;
import com.johnkuper.database.DBSaver;
import com.johnkuper.model.Payment;
import com.johnkuper.parser.PaymentParser;
import com.johnkuper.remover.FileRemover;
import com.johnkuper.storage.Storage;
import com.johnkuper.watcher.WatchDir;

public class ThreadsManager {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private static final String WATCHER_PATH = "src/main/resources/xml";
	private static final Integer STORAGE_CAPACITY = 1000000;
	private static final Integer SAVER_THREADS_AMOUNT = 10;
	private Storage<Path> pathStorage = new Storage<>(STORAGE_CAPACITY);
	private Storage<Payment> paymentStorage = new Storage<>(STORAGE_CAPACITY);
	private Storage<Future<Path>> deletePathStorage = new Storage<>(
			STORAGE_CAPACITY);
	private ExecutorService executor;
	private IConnectionProvider provider;

	public ThreadsManager(IConnectionProvider provider, ExecutorService executor) {
		this.provider = provider;
		this.executor = executor;
	}

	public void startAllTasks() {
		runWatcher();
		runFileRemover();
		runSaver();
		runParser();

	}

	private void runWatcher() {
		logger.debug("Start 'runWatcher'");

		Path dir = Paths.get(WATCHER_PATH);
		try {
			executor.execute(new WatchDir(dir, pathStorage));
		} catch (IOException e) {
			String msg = "Error during 'runWatcher'";
			logger.error(msg, e);
		}

	}

	private void runSaver() {
		logger.debug("Start 'runSaver'");
		int x;
		for (x = 0; x < SAVER_THREADS_AMOUNT; x++) {
			executor.submit(new DBSaver(paymentStorage, provider));
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

}
