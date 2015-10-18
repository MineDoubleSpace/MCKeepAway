package me.mdspace.arena;

import me.mdspace.keepaway.KeepAway;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		for (Arena arena : Arena.getArenas()) {
			if (player == arena.getTarget()) {
				arena.setTarget(player.getKiller());
				event.setDroppedExp(0);
				event.getDrops().clear();
			}
		}
		event.setDeathMessage("");
		event.setDroppedExp(0);
		event.getDrops().clear();
		return;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		for (Arena arena : Arena.getArenas()) {
			for (Player pl : arena.getPlayers()) {
				if (player == pl) {
					event.setRespawnLocation(arena.getStartPoint());
					arena.addItems();
					return;
				}
			}
		}
		event.setRespawnLocation(KeepAway.get().getLobby());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (!(event.getDamager() instanceof Player)) {
			event.setCancelled(true);
			return;
		}
		Player entity = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();
		for (Arena arena : Arena.getArenas()) {
			if (damager == arena.getTarget()) {
				return;
			}
			if (entity == arena.getTarget()) {
				return;
			}
		}
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayer(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "      Welcome to KeepAway");
		event.getPlayer().teleport(KeepAway.get().getLobby());
	}

	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent event) {
		event.setQuitMessage("");
		for (Arena arena : Arena.getArenas()) {
			for (Player pl : arena.getPlayers()) {
				if (event.getPlayer() == pl) {
					arena.removePlayer(pl);
				}
			}
		}
	}
}
