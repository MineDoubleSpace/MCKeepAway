package me.mdspace.arena;

import me.mdspace.keepaway.BoardUtil;
import me.mdspace.keepaway.KeepAway;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArenaEnd implements Runnable {
	
	private ArenaTask task;

	public ArenaEnd(ArenaTask task) {
		this.task = task;
	}

	public void run() {
		BoardUtil.createBoard(task.getArena());
		for (Player pl : task.getArena().getPlayers()) {
			pl.teleport(KeepAway.get().getLobby());
			pl.getInventory().clear();
			pl.getInventory().setArmorContents(null);
			pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
		task.getArena().reset();
		task.startRun();
	}
}
