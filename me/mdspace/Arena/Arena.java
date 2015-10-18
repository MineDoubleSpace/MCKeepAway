package me.mdspace.arena;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import me.mdspace.keepaway.BoardUtil;
import me.mdspace.keepaway.KeepAway;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.collect.Lists;

public class Arena {

	private static List<Arena> arena = Lists.newArrayList();
	private int id;
	private Location startPoint;
	private String name;
	private ArenaState state;
	private List<Player> players = Lists.newArrayList();
	private int runTime;
	private World world;
	private Player target;
	private int playerAmount;
	private HashMap<Player, Integer> score = new HashMap<>();
	private Scoreboard scoreboard;
	private ArenaTask task;
	private String path;

	public Arena(int id, String name, int runTime, int playerAmount, String path) {
		this.id = id;
		this.name = name;
		this.state = ArenaState.WAITING;
		this.runTime = runTime;
		this.playerAmount = playerAmount;
		this.path = path;
		createWorld();
		setSpawn();
		this.scoreboard = BoardUtil.createBoard(this);
		addArena();
		this.task = new ArenaTask(this);
		task.startRun();

	}

	public void addPlayer(Player player) {
		if (players.contains(player)) return;
		for (Arena arena : getArenas()) {
			for (Player pl : arena.getPlayers()) {
				if (player == pl) {
					player.sendMessage(ChatColor.RED + "You may not join an arena while in another arena.");
					return;
				}
			}
		}
		players.add(player);
		player.sendMessage(ChatColor.GREEN + "You joined " + name);
		player.sendMessage(ChatColor.GREEN + "Please wait " + name + " is starting soon");
	}

	public void addArena() {
		arena.add(this);
	}

	public void removePlayer(Player player) {
		if (players.contains(player)) players.remove(player);
	}

	public void reset() {
		this.state = ArenaState.ENDING;
		this.players.clear();
		this.score.clear();
		this.target = null;
		this.state = ArenaState.WAITING;
		for (Player pl : getPlayers()) {
			pl.teleport(KeepAway.get().getLobby());
		}
	}

	public int getID() {
		return id;
	}

	public void setState(ArenaState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public ArenaState getState() {
		return state;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public static Arena getArena(int id) {
		for (Arena a : arena) {
			if (a.getID() == id) return a;
		}
		return null;
	}

	public Location getStartPoint() {
		return startPoint;
	}

	public int getRunTime() {
		return runTime;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
		target.sendMessage(ChatColor.GOLD + "You are now the holder!");
		AddItemTarget();
		addItems();
	}

	public int getPlayerAmount() {
		return playerAmount;
	}

	public Integer getScore(Player player) {
		return score.get(player);
	}
	
	public String getPath() {
		return path;
	}

	public void updateScore() {

	}

	public World getWorld() {
		return world;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public static List<Arena> getArenas() {
		return arena;
	}

	public List<String> getTop() {
		List<String> data = Lists.newArrayList();
		for (Entry<Player, Integer> e : score.entrySet()) {
			data.add(e.getValue() + " " + e.getKey().getName());
		}

		Collections.sort(data, new Comparator<String>() {
			public int compare(String a, String b) {
				int aVal = Integer.parseInt(a.split(" ")[0]);
				int bVal = Integer.parseInt(b.split(" ")[0]);

				return Integer.compare(aVal, bVal);
			}
		});
		List<String> top = Lists.newArrayList();
		for (int i = (data.size() > 10 ? 10 : data.size()) - 1; i >= 0; i--) {
			String line = data.get(i);
			String player = line.split(" ")[1], bal = line.split(" ")[0];

			top.add(player + " : " + bal);
		}
		return top;
	}

	public void setScore(Player player, int score) {
		this.score.put(player, score);
	}

	public void setStartPoint(Location loc) {
		this.startPoint = loc;
	}

	private void setSpawn() {
		try {
			FileConfiguration config = KeepAway.get().getConfig();
			World world = this.world;
			double x = config.getDouble(path + ".spawn.x");
			double y = config.getDouble(path + ".spawn.y");
			double z = config.getDouble(path + ".spawn.z");
			this.startPoint = new Location(world, x, y, z);

			if (startPoint == null) {
				this.startPoint = world.getSpawnLocation();
			}
		} catch (Exception e) {
			this.startPoint = world.getSpawnLocation();
		}
	}

	private void createWorld() {
		world = Bukkit.getWorld(name + " " + id);
		if (world == null) {
			world = Bukkit.getServer().createWorld(new WorldCreator(name + " " + id));
		}
	}

	public int getTimeRemain() {
		return runTime - task.getTimer();
	}

	private void AddItemTarget() {
		target.getInventory().clear();
		target.getInventory().setArmorContents(null);
		ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
		ItemStack head = new ItemStack(Material.EMERALD_BLOCK, 1);
		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS, 1);
		ItemStack leg = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		target.getInventory().setHelmet(head);
		target.getInventory().setChestplate(chest);
		target.getInventory().setLeggings(leg);
		target.getInventory().setBoots(boot);
		target.getInventory().addItem(item);
		target.getInventory().addItem(sword);
	}

	public void addItems() {
		for (Player pl : getPlayers()) {
			if (pl != target) {
				pl.getInventory().clear();
				pl.getInventory().setArmorContents(null);
				ItemStack head = new ItemStack(Material.WOOL, 1);
				ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
				ItemStack boot = new ItemStack(Material.LEATHER_BOOTS, 1);
				ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
				ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
				pl.getInventory().setHelmet(head);
				pl.getInventory().setChestplate(chest);
				pl.getInventory().setLeggings(leg);
				pl.getInventory().setBoots(boot);
				pl.getInventory().addItem(sword);
			}
		}
	}

}
