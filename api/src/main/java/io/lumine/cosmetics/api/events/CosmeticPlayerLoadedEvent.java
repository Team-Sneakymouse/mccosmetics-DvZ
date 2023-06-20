package io.lumine.cosmetics.api.events;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CosmeticPlayerLoadedEvent extends Event {

    @Getter private final Player player;
    @Getter private final CosmeticProfile profile;
    
    public CosmeticPlayerLoadedEvent(Player player, CosmeticProfile profile) {
    	super(false);
    	this.player = player;
        this.profile = profile;
    }

    /*
     * Standard event stuff
     */
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;
    
    public void setCancelled(boolean bool)   {
        this.canceled = bool;
    }
    
    public boolean isCancelled() {
        return this.canceled;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}