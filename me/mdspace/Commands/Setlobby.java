package me.mdspace.Commands;

import me.mdspace.arena.ArenaManager;
import me.mdspace.keepaway.KeepAway;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setlobby extends Commands {

	public Setlobby() {
		super("setlobby", "Sets lobby location", "keepaway.admin", "");
	}
	
	@Override
	public void run(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		ArenaManager.get().setLocation("lobby.", player.getLocation());
		KeepAway.get().setLobby(player.getLocation());
		player.sendMessage(ChatColor.GREEN + "Lobby location updated!");
	}
	

}
