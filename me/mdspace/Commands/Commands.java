package me.mdspace.Commands;

import org.bukkit.command.CommandSender;

public abstract class Commands {

	private String command;
	private String permissions;
	private String args;
	private String description;
	private int min;

	public Commands(String command, String description, String permissions, String args) {
		this(command, description, permissions, args, 0);
	}

	public Commands(String command, String description, String permissions, String args, int min) {
		this.command = command;
		this.permissions = permissions;
		this.args = args;
		this.description = description;
		this.min = min;
	}

	public String getName() {
		return command;
	}

	public String getPermissions() {
		return permissions;
	}

	public String getDescription() {
		return description;
	}

	public String getArgs() {
		return args;
	}

	public int getMin() {
		return min;
	}

	public abstract void run(CommandSender sender, String[] args);

}
