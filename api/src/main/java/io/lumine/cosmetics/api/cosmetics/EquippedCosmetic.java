package io.lumine.cosmetics.api.cosmetics;

import io.lumine.utils.serialize.Chroma;
import lombok.Data;

@Data
public class EquippedCosmetic {

    private final Cosmetic cosmetic;
    private final CosmeticVariant variant;
    private final Chroma color;
    
    public EquippedCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
        this.variant = null;
        this.color = null;
    }    
    
    public EquippedCosmetic(CosmeticVariant variant) {
        this.cosmetic = variant.getCosmetic();
        this.variant = variant;
        this.color = null;
    }
    
    public EquippedCosmetic(Cosmetic cosmetic, Chroma color) {
        this.cosmetic = cosmetic;
        this.variant = null;
        this.color = color;
    }    
    
}
