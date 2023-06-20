package io.lumine.cosmetics.api.players.wardrobe;

import org.bukkit.entity.Player;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

public interface WardrobeTracker {

    public WardrobeManager getManager();
    
    public Player getPlayer();
    
    public Mannequin getMannequin();
    
    public void setCleanupWhenDone(Class<? extends Cosmetic> type);
    
}
