package com.johnkuper.watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnkuper.storage.Storage;

public class WatchDir implements Runnable {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private boolean trace = false;
	private Storage<Path> pathStorage;
	final static Logger logger = LoggerFactory.getLogger("JohnKuper");

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	@Override
	public void run() {
		logger.debug("Start 'WatchDir' thread");

		processEvents();

	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE);
		if (trace) {
			Path prev = keys.get(key);
			if (prev == null) {
				logger.debug("Directory register for Watcher: {}\n", dir);
			} else {
				if (!dir.equals(prev)) {
					System.out.format("update: %s -> %s\n", prev, dir);
				}
			}
		}
		keys.put(key, dir);
	}

	/**
	 * Creates a WatchService and registers the given directory
	 */
	public WatchDir(Path dir, Storage<Path> pathstorage) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		this.trace = true;
		this.pathStorage = pathstorage;
		register(dir);
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	private void processEvents() {

		while (true) {

			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				logger.debug("'WatchDir' thread was interrupted!");
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					continue;
				}

				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path fullpath = dir.resolve(name);
				if (kind == ENTRY_CREATE) {
					putPathToStorage(fullpath);
					logger.debug("Fullpath '{}' add in pathStorage", fullpath);
					System.out.format("%s: %s\n", event.kind().name(), name);
				}

			}

			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);
			}
		}

	}

	private void putPathToStorage(Path path) {
		try {
			pathStorage.put(path);
		} catch (InterruptedException e) {
			String msg = String.format("Thread %s was interrupted", Thread
					.currentThread().getClass());
			logger.debug(msg, e);
		}
	}

}
