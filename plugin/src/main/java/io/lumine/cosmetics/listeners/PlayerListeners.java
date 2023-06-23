package io.lumine.cosmetics.listeners;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.bukkit.entity.Player;

public class PlayerListeners implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		MCCosmeticsPlugin.inst().getVolatileCodeHandler().injectPlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		MCCosmeticsPlugin.inst().getVolatileCodeHandler().removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerMounted(EntityMountEvent event) {
		if (!event.isCancelled() && event.getMount() instanceof Player player) {
			MCCosmeticsPlugin.inst().getBackManager().getHelper().reapply(MCCosmeticsPlugin.inst().getProfiles().getProfile(player));
		}
	}

	@EventHandler
	public void onPlayerDisounted(EntityDismountEvent event) {
		if (!event.isCancelled() && event.getDismounted() instanceof Player player) {
			MCCosmeticsPlugin.inst().getBackManager().getHelper().reapply(MCCosmeticsPlugin.inst().getProfiles().getProfile(player));
		}
	}
}
