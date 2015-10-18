package me.mdspace.Commands;

import me.mdspace.arena.Arena;
import me.mdspace.arena.ArenaManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setspawn extends Commands {

	public Setspawn() {
		super("setspawn", "set arena spawn", "keepaway.admin", "<ID>", 1);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		int id;
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Invalid ID");
			return;
		}
		Arena arena = Arena.getArena(id);
		if (arena == null) {
			sender.sendMessage(ChatColor.RED + "Invalid ID");
			return;
		}
		Player player = (Player) sender;
		ArenaManager.get().setLocation(arena.getPath() + ".spawn.", player.getLocation());
		arena.setStartPoint(player.getLocation());
		player.sendMessage(ChatColor.GREEN + "Spawn location updated!");
	}

}
