package io.lumine.cosmetics.nms.cosmetic;

import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;

public interface VolatileEquipmentHelper extends VolatileCosmeticHelper {

	void apply(CosmeticProfile profile);
	void unapply(CosmeticProfile profile);
	
	default void equipMannequin(Mannequin mannequin, EquippedCosmetic cosmetic) {
	    
	}
	
    default void unequipMannequin(Mannequin mannequin) {
        
    }

	static byte toByte(float val) {
		return (byte)((int)(val * 256.0F / 360.0F));
	}

}
