package me.mdspace.Commands;

import me.mdspace.arena.Arena;
import me.mdspace.arena.ArenaState;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join extends Commands {

	public Join() {
		super("join", "Join an arena", "keepaway.player", "<ID>");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
			for (Arena arena : Arena.getArenas()) {
				if (arena.getState() == ArenaState.WAITING) {
					arena.addPlayer((Player) sender);
					return;
				}
			}
			sender.sendMessage(ChatColor.RED + "Could not find an arnea, please try again later");
			return;
		}
		int id;
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage("Invalid ID");
			return;
		}
		Arena arena = Arena.getArena(id);
		if (arena == null) {
			sender.sendMessage("Invalid ID");
			return;
		}

		if (arena.getState() != ArenaState.WAITING) {
			sender.sendMessage(ChatColor.RED + "Game Already started!");
			return;
		}

		arena.addPlayer((Player) sender);
	}
}
