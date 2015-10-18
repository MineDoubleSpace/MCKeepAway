package me.mdspace.Commands;

import me.mdspace.arena.Arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport extends Commands {

	public Teleport() {
		super("tp", "Teleport to arena", "keepaway.admin", "<id>", 1);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		int id;
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage("Invalid ID");
			return;
		}
		Arena arena = Arena.getArena(id);
		if (arena == null) {
			for (Arena s : Arena.getArenas()) {
				sender.sendMessage(s.getName() + s.getID());
			}
			sender.sendMessage("Could not find the arena by ID : " + id);
			return;
		}
		Player player = (Player) sender;
		player.teleport(arena.getStartPoint());

	}

}
