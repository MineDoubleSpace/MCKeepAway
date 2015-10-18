package me.mdspace.Commands;

import me.mdspace.keepaway.KeepAway;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave extends Commands {

	public Leave() {
		super("leave", "Leave the current arena", "keepaway.player", "");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		player.sendMessage(ChatColor.GREEN + "Leaving the arena");
		player.teleport(KeepAway.get().getLobby());
	}

}
