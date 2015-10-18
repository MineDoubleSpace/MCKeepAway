package me.mdspace.arena;

import java.util.Random;

import me.mdspace.keepaway.BoardUtil;
import me.mdspace.keepaway.KeepAway;
import me.mdspace.keepaway.Log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class ArenaTask {

	private Arena arena;
	private ArenaRunnable runnable;
	private int timer;
	private int task;
	public int startTimer;
	public boolean count = false;

	//Execute this task every second for arena
	public void onSecond() {
		if (arena.getState() == ArenaState.ENDING) return;
		try {
			if (hasStarted()) {
				timer++;
				if (arena.getPlayers().size() <= 0) {
					end();
					return;
				}

				if (timer > arena.getRunTime()) {
					end();
					return;
				}
				addPointsToTarget();
				BoardUtil.updateBoard(arena);
				return;
			}
			if (!hasStarted()) {
				if (arena.getPlayerAmount() > arena.getPlayers().size()) {
					arena.setState(ArenaState.WAITING);
					return;
				}
				startStartTimer();
				return;
			}
		} catch (Exception e) {
			Log.log(e);
		}
	}

	public void start() {
		Bukkit.getScheduler().cancelTask(startTimer);
		arena.setState(ArenaState.STARTED);
		arena.setTarget(arena.getPlayers().get(new Random().nextInt(arena.getPlayers().size())));
		for (Player pl : arena.getPlayers()) {
			pl.teleport(arena.getStartPoint());
			arena.setScore(pl, 0);
		}
		BoardUtil.updateBoard(arena);
	}

	public void addPointsToTarget() {
		Player target = arena.getTarget();
		if (target == null) {
			arena.setTarget(arena.getPlayers().get(new Random().nextInt(arena.getPlayers().size())));
		}
		arena.setScore(target, arena.getScore(target) + 1);
		playFirework(target);
	}

	public ArenaTask(Arena arena) {
		this.arena = arena;
		runnable = new ArenaRunnable(this);
		timer = 0;
		startRun();
	}

	public void startRun() {
		try {
			this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(KeepAway.get(), runnable, 20L, 20L);
		} catch (Exception e) {
			Log.log(e);
		}
	}

	public int getTimer() {
		return timer;
	}

	public void startStartTimer() {
		if (!count) {
			count = true;
			startTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(KeepAway.get(), new StartTimer(), 20L, 20L);
		}
	}

	public void end() {
		arena.setState(ArenaState.ENDING);
		Bukkit.getScheduler().cancelTask(task);
		this.timer = 0;
		for (Player pl : arena.getPlayers()) {
			pl.sendMessage("       " + ChatColor.GOLD + "" + ChatColor.BOLD + " Top Players");
			for (String s : arena.getTop()) {
				pl.sendMessage(ChatColor.YELLOW + s);
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(KeepAway.get(), new ArenaEnd(this), 100L);

	}

	public void playFirework() {
		Firework firework = (Firework) arena.getWorld().spawn(arena.getTarget().getLocation().add(0, 2, 0), Firework.class);

		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(Type.CREEPER).withColor(Color.GREEN).withFade(Color.BLUE).build());
		meta.setPower(0);
		firework.setFireworkMeta(meta);
	}

	public Arena getArena() {
		return arena;
	}

	public boolean hasStarted() {
		return arena.getState() == ArenaState.STARTED;
	}

	private void playFirework(Player player) {
		Location loc = player.getLocation().add(0, 2, 0);
		Firework firework = (Firework) player.getWorld().spawn(loc, Firework.class);

		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(Type.BALL).withColor(Color.GREEN).build());
		meta.setPower(0);
		firework.setFireworkMeta(meta);
	}

	public class StartTimer implements Runnable {

		public int timer = 5;

		public void run() {
			for (Player pl : arena.getPlayers()) {
				pl.sendMessage(ChatColor.BLUE + arena.getName() + " starting in " + ChatColor.GOLD + timer + " seconds!");
			}
			timer--;
			if (timer <= 0) {
				start();
			}
		}

	}

}
