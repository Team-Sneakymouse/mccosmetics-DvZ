package io.lumine.cosmetics.api.cosmetics.pets;

import org.bukkit.entity.Player;

public interface PetHook {

    boolean isValid();

    void equip(Player player);

}
