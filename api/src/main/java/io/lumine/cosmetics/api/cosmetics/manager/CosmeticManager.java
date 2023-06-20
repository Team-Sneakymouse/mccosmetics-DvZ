package io.lumine.cosmetics.api.cosmetics.manager;

import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;

public interface CosmeticManager {

    void equip(CosmeticProfile profile);
    void unequip(CosmeticProfile profile);
    
    void equipMannequin(Mannequin mannequin, EquippedCosmetic cosmetic);
    void unequipMannequin(Mannequin mannequin);
    
}
