package com.johnkuper.storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Storage<T> {

	private BlockingQueue<T> storage;

	public Storage(int capacity) {
		storage = new LinkedBlockingQueue<>(capacity);
	}

	public T get() throws InterruptedException {
		return storage.take();
	}

	public void put(T value) throws InterruptedException {
		storage.put(value);
	}
}