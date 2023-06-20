package io.lumine.cosmetics.listeners;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		MCCosmeticsPlugin.inst().getVolatileCodeHandler().injectPlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		MCCosmeticsPlugin.inst().getVolatileCodeHandler().removePlayer(event.getPlayer());
	}

}
