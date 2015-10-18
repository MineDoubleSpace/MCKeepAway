package me.mdspace.keepaway;

import me.mdspace.arena.Arena;
import me.mdspace.arena.ArenaState;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class BoardUtil {

	public static Scoreboard updateBoard(Arena arena) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = board.registerNewObjective("Board", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.RED + "Time : " + ChatColor.GREEN + arena.getTimeRemain());
		for (Player pl : arena.getPlayers()) {
			Score score = objective.getScore(pl);
			if (arena.getState() == ArenaState.STARTED || arena.getState() == ArenaState.ENDING) {
				score.setScore(arena.getScore(pl));
			} else {
				score.setScore(0);
			}
			pl.setScoreboard(board);
		}
		return board;
	}

	public static Scoreboard createBoard(Arena arena) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = board.registerNewObjective("Board", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + arena.getName());
		return board;
	}

}
