package me.mdspace.keepaway;

import me.mdspace.arena.ArenaListener;
import me.mdspace.arena.ArenaManager;
import net.minecraft.server.v1_7_R1.MinecraftServer;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class KeepAway extends JavaPlugin {

	static KeepAway instance;
	private Location lobby;
	private ArenaListener listener;
	private String version = "1.7.2";

	@Override
	public void onEnable() {
		instance = this;
		Log.setDebugging(true);
		listener = new ArenaListener();
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("keepaway").setExecutor(new CommandManager());
		String serverVersion = MinecraftServer.getServer().getVersion();
		if (!serverVersion.equals(version)) {
			setEnabled(false);
			for (int i = 0; i < 10; i++) {
				System.err.println("-------------- Error! Error! Error! ------------");
				System.err.println("The plugin is not updated? please contact MineDoubleSpace on skype");
				System.err.println("Skype : minedoublespace.lol");
				System.err.println("Shutting down the server to prevent database crashes!");
				System.err.println("----------------------------------------------");
			}
			getServer().shutdown();
			return;
		}

		reload();
	}

	@Override
	public void onDisable() {
	}

	public void reload() {
		try {
			saveDefaultConfig();
			reloadConfig();
			ArenaManager.get().LoadArenaFromConfig();
			ArenaManager.get().LoadLobbyLocation();
		} catch (Exception e) {
			Log.log(e);
		}
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	public static KeepAway get() {
		return instance;
	}

}
