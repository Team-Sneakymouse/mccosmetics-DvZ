package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.api.players.wardrobe.WardrobeTracker;
import io.lumine.cosmetics.nms.cosmetic.VolatileCosmeticHelper;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface VolatileCodeHandler {

    VolatileCosmeticHelper getCosmeticHelper(Class<? extends Cosmetic> tClass);
    Collection<VolatileCosmeticHelper> getCosmeticHelpers();

    void injectPlayer(Player player);
    
    void removePlayer(Player player);

    default Mannequin createMannequin(WardrobeTracker tracker, Player player, Location location) { return null; }
    
    void removeFakeEntity(int id);

    void setBodyYaw(LivingEntity entity, double yaw);
    
    float getBodyYaw(LivingEntity entity);

}
