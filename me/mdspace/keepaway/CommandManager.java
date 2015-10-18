package me.mdspace.keepaway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mdspace.Commands.Arenas;
import me.mdspace.Commands.Commands;
import me.mdspace.Commands.Join;
import me.mdspace.Commands.Leave;
import me.mdspace.Commands.Setlobby;
import me.mdspace.Commands.Setspawn;
import me.mdspace.Commands.Teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class CommandManager implements CommandExecutor {

	public static List<Commands> commands = Lists.newArrayList();

	public CommandManager() {
		commands.add(new Teleport());
		commands.add(new Join());
		commands.add(new Leave());
		commands.add(new Setlobby());
		commands.add(new Arenas());
		commands.add(new Setspawn());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "      ---------   Keepaway help   ---------   ");
			for (Commands c : commands) {
				if (!sender.hasPermission(c.getPermissions())) {
					continue;
				}
				sender.sendMessage(ChatColor.YELLOW + "/keepaway " + c.getName() + " " + c.getArgs() + " - " + ChatColor.GREEN + c.getDescription());
			}
			sender.sendMessage(ChatColor.GOLD + "      ----------------------   ");
			return true;
		}

		ArrayList<String> a = new ArrayList<String>(Arrays.asList(args));
		a.remove(0);

		for (Commands c : commands) {
			if (c.getName().equalsIgnoreCase(args[0])) {
				if (!sender.hasPermission(c.getPermissions())) {
					sender.sendMessage(ChatColor.RED + "No permissions!");
					return true;
				}
				try {
					c.run(sender, a.toArray(new String[a.size()]));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "An error has occurred.");
					e.printStackTrace();
				}
				return true;
			}
		}

		sender.sendMessage(ChatColor.RED + "Invalid command!");
		return true;
	}
}
