package io.lumine.cosmetics.api.players.wardrobe;

import org.bukkit.entity.Player;

public interface WardrobeManager {

    public void openWardrobe(Player player);
    
    public void closeWardrobe(Player player);
    
    public boolean isInWardrobe(Player player);
    
    public Mannequin getMannequin(Player player);
    
}
