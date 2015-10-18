package me.mdspace.arena;

import me.mdspace.keepaway.KeepAway;
import me.mdspace.keepaway.Log;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaManager {

	public static ArenaManager instance;

	public void LoadArenaFromConfig() {
		FileConfiguration config = KeepAway.get().getConfig();
		for (String s : config.getConfigurationSection("arena").getKeys(false)) {
			try {
				Log.log("Starting Arena : " + s + " Please wait..");
				String path = "arena." + s;
				int id = config.getInt(path + ".id", 1);
				int runTime = config.getInt(path + ".runtime", 300);
				int playerAmount = config.getInt(path + ".min-players", 5);
				String name = config.getString(path + ".name", "default arena");
				Arena arena = new Arena(id, name, runTime, playerAmount, path);
				arena.setState(ArenaState.DISABLED);
				Log.log("Arena " + s + " successfully started");
				arena.setState(ArenaState.WAITING);
			} catch (Exception e) {
				Log.log("Failed to start arena : " + s);
				Log.log(e);
			}
		}
	}

	public void LoadLobbyLocation() {
		KeepAway.get().setLobby(getLocation("lobby."));
	}

	public static Location getLocation(String path) {
		FileConfiguration config = KeepAway.get().getConfig();
		World world = Bukkit.getWorld(config.getString(path + "name"));
		double x = config.getDouble(path + "x");
		double y = config.getDouble(path + "y");
		double z = config.getDouble(path + "z");
		float pitch = (float) config.getDouble(path + "pitch");
		float yaw = (float) config.getDouble(path + "yaw");
		return new Location(world, x, y, z, yaw, pitch);
	}

	public Location getLocation(String path, Location loc) {
		try {
			Location location = getLocation(path);
			return location;
		} catch (Exception e) {
			return loc;
		}
	}

	public void setLocation(String path, Location loc) {
		FileConfiguration config = KeepAway.get().getConfig();
		config.set(path + "x", loc.getX());
		config.set(path + "y", loc.getY());
		config.set(path + "z", loc.getZ());
		config.set(path + "pitch", loc.getPitch());
		config.set(path + "yaw", loc.getYaw());
		KeepAway.get().saveConfig();
	}

	public static ArenaManager get() {
		if (instance == null) instance = new ArenaManager();
		return instance;
	}

}
