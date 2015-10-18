package me.mdspace.arena;

public class ArenaRunnable implements Runnable {
	private ArenaTask task;

	public ArenaRunnable(ArenaTask task) {
		this.task = task;
	}

	public void run() {
		task.onSecond();
	}

}
