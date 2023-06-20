package io.lumine.cosmetics.api.players.wardrobe;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

public interface Mannequin {

    public int getEntityId();
    
    public UUID getUniqueId();
    
    public Player getPlayer();

    public Location getLocation();
    
    public float getRotation();
    
    public WardrobeTracker getTracker();
    
    public void despawn();
    
    default public void setCleanupWhenDone(Class<? extends Cosmetic> type) {
        getTracker().setCleanupWhenDone(type);
    }
    
    public void addExtraEntity(Class<? extends Cosmetic> type, int id);
    
    public void removeExtraEntity(Class<? extends Cosmetic> type);
        
}
