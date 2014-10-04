package com.johnkuper.remover;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.storage.Storage;

public class FileRemover implements Runnable {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");

	private Storage<Future<Path>> deletePathStorage;

	public FileRemover(Storage<Future<Path>> storage) {
		this.deletePathStorage = storage;
	}

	@Override
	public void run() {
		logger.debug("Start 'FileRemover' thread");
		try {
			deleteTask();
		} catch (InterruptedException e) {
			String msg = "'FileRemover' thread was interrupted";
			logger.error(msg, e);

		}
	}

	private void deleteTask() throws InterruptedException {
		Future<Path> path = deletePathStorage.get();
		while (true) {
			try {

				if (path.isDone()) {
					Files.deleteIfExists(path.get());
					logger.debug("File '{}' was deleted", path.get());
				} else {
					logger.debug(
							"File '{}' still parsing. put it back to storage!!!",
							path.get());
					deletePathStorage.put(path);
				}

			} catch (IOException e) {
				deletePathStorage.put(path);
				logger.debug("Error during 'deleteTask. Put file back into deletePathStorage {}",e);

			} catch (InterruptedException e) {
				String msg = "'FileRemover' thread was interrupted";
				logger.error(msg, e);

			} catch (ExecutionException e) {
				String msg = "ExecutionException during 'deleteTask'";
				logger.error(msg, e);
			}
		}
	}
}
