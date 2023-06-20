package io.lumine.cosmetics.api.players;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.utils.serialize.Chroma;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface CosmeticProfile {

    public UUID getUniqueId();
    
    public String getName();
    
    public Player getPlayer();
    
    public boolean has(Cosmetic cosmetic);
    
    public void equip(Cosmetic cosmetic);

    public void equip(CosmeticVariant cosmetic);
    
    public void equip(Cosmetic cosmetic, Chroma color);

    public void unequip(Cosmetic cosmetic);

    public void unequip(Class<? extends Cosmetic> tClass);
    
    public Optional<EquippedCosmetic> getEquipped(Class<? extends Cosmetic> clazz);   

    public boolean isEquipped(Cosmetic cosmetic);

    void setHidden(Class<? extends Cosmetic> cosmetic, boolean flag);

    boolean isHidden(Class<? extends Cosmetic> cosmetic);

}
