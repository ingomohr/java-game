package org.ingomohr.game.util;

/**
 * Demonstrates how a {@link ThreadPool} works.
 */
public class ThreadPoolDemo {

	public static void main(String[] args) {
		boolean fetchedParameters = false;
		int numTasks = 0;
		int numThreads = 0;

		if (args.length == 2) {
			try {
				numTasks = Integer.parseInt(args[0]);
				numThreads = Integer.parseInt(args[1]);
				fetchedParameters = true;
			} catch (NumberFormatException ex) {
			}
		}

		if (fetchedParameters) {
			run(numTasks, numThreads);
		} else {
			printUsage();
		}
	}

	private static void run(int numTasks, int numThreads) {
		ThreadPool pool = new ThreadPool(numThreads);

		for (int i = 1; i <= numTasks; i++) {
			pool.runTask(createTask(i));
		}

		pool.join();
	}

	private static Runnable createTask(int id) {
		return new Runnable() {

			@Override
			public void run() {
				System.out.println("Task-" + id + ": start");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Task-" + id + ": end");
			}
		};
	}

	private static void printUsage() {
		System.out.println("Usage: ThreadPoolTest numTasks numThreads");
		System.out.println("  numTasks - integer: number of tasks to run");
		System.out.println("  numThreads - integer: number of threads to use");
	}

}
