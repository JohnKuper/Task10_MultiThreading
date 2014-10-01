package com.johnkuper.manager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.storage.Storage;
import com.johnkuper.watcher.WatchDir;

public class ThreadsManager {

	final static Logger logger = LoggerFactory.getLogger("JohnKuper");
	private Storage<Path> pathStorage = new Storage<>(100);

	public void runWatcher() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Path dir = Paths.get("src/main/resources/xml");
		try {
			executorService.execute(new WatchDir(dir, pathStorage));
			long threadid = Thread.currentThread().getId();
			logger.debug("Thread's ID = {}", threadid);
		} catch (IOException e) {
			String msg = "Error during runWatcher";
			logger.error(msg, e);
		}

	}

}
