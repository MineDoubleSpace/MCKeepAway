package me.mdspace.Commands;

import me.mdspace.arena.Arena;
import me.mdspace.arena.ArenaState;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Arenas extends Commands {

	public Arenas() {
		super("arena", "Arena information", "keepaway.player", "");
	}

	public void run(CommandSender sender, String[] args) {
		for (Arena arena : Arena.getArenas()) {
			ChatColor stateColor = ChatColor.RED;
			if (arena.getState() == ArenaState.WAITING) {
				stateColor = ChatColor.DARK_GREEN;
			}
			sender.sendMessage(ChatColor.BLUE + "ID : " + ChatColor.GOLD + arena.getID() + ChatColor.BLUE + " Arena State : " + stateColor + arena.getState().toString());
		}

	}
}
