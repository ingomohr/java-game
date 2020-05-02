package org.ingomohr.game.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Group of limited number of threads used to execute tasks.
 */
public class ThreadPool extends ThreadGroup {

	private static int id;
	private boolean isAlive;
	private final List<Runnable> taskQueue;

	public ThreadPool(int numberOfThreads) {
		super("ThreadPool " + (id++));
		setDaemon(true);

		isAlive = true;

		taskQueue = new LinkedList<>();

		IntStream.range(1, numberOfThreads).forEach((num) -> new PooledThread().start());
	}

	/**
	 * Requests the given task to run.
	 * <p>
	 * Schedules the given task and returns immediately.
	 * </p>
	 * 
	 * @param runnable the task to run. Cannot be <code>null</code>.
	 */
	public synchronized void runTask(Runnable runnable) {
		if (!isAlive) {
			throw new IllegalStateException("Pool is not alive");
		}

		Objects.requireNonNull(runnable);
		taskQueue.add(runnable);
		notify();
	}

	/**
	 * Returns the next task in line.
	 * 
	 * @return <code>null</code> if the pool has been stopped.
	 * @throws InterruptedException if active thread has been interrupted while
	 *                              waiting.
	 */
	protected synchronized Runnable getTask() throws InterruptedException {
		while (taskQueue.size() == 0) {
			if (!isAlive) {
				return null;
			}
			wait();
		}
		return taskQueue.remove(0);
	}

	/**
	 * Closes the pool and returns immediately.
	 */
	public synchronized void close() {
		if (isAlive) {
			isAlive = false;
			taskQueue.clear();
			interrupt();
		}
	}

	/**
	 * Closes the pool and waits for all running threads to finish.
	 */
	public void join() {
		System.out.println("notify...");
		synchronized (this) {
			isAlive = false;
			notifyAll();
		}
		
		System.out.println("Join all...");

		Thread[] threads = new Thread[activeCount()];
		int count = enumerate(threads);
		for (int i = 0; i < count; i++) {
			try {
				System.out.println("Join " + i + "...");
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private class PooledThread extends Thread {

		public PooledThread() {
			super(ThreadPool.this, "PooledThread-" + (id++));
		}

		public void run() {

			while (!isInterrupted()) {
				Runnable task = null;

				try {
					task = getTask();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (task != null) {
					try {
						task.run();
					} catch (Throwable ex) {
						uncaughtException(this, ex);
					}
				}
			}
		}

	}

}
