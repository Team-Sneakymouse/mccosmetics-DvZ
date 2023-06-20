package io.lumine.cosmetics.players;

import java.util.Optional;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.storage.sql.mappings.tables.records.ProfileEquippedRecord;
import io.lumine.utils.serialize.Chroma;
import lombok.Data;

@Data
public class ProfileCosmeticData {

    private String type;
    private String id;
    private String variant;
    
    public ProfileCosmeticData(Cosmetic cosmetic) {
        this.type = cosmetic.getType();
        this.id = cosmetic.getId();
        this.variant = "default";
    }

    public ProfileCosmeticData(CosmeticVariant variant) {
        this.type = variant.getCosmetic().getType();
        this.id = variant.getCosmetic().getId();
        this.variant = variant.getKey();
    }
    
    public ProfileCosmeticData(Cosmetic cosmetic, Chroma color) {
        this.type = cosmetic.getType();
        this.id = cosmetic.getId();
        this.variant = color.toHexString();
    }
    
    public ProfileCosmeticData(ProfileEquippedRecord equipRecord) {
        this.type = equipRecord.getSlot();
        this.id = equipRecord.getCosmeticId();
        this.variant = equipRecord.getCosmeticData();
    }

    public Optional<EquippedCosmetic> toEquippedCosmetic() {
        var maybeManager = MCCosmeticsPlugin.inst().getCosmetics().getManager(type);
        if(maybeManager.isEmpty()) {
            return Optional.empty();
        }
        
        var maybeCosmetic = maybeManager.get().getCosmetic(id);
        if(maybeCosmetic.isEmpty()) {
            return Optional.empty();
        }
        var cosmetic = (Cosmetic) maybeCosmetic.get();
        
        if(variant != null) {
            if(variant.startsWith("#")) {
                return Optional.of(new EquippedCosmetic(cosmetic, Chroma.of(variant)));
            } else if(cosmetic.hasVariants()) {
                var maybeVariant = cosmetic.getVariant(this.variant);
                
                if(maybeVariant.isPresent()) {
                    return Optional.of(new EquippedCosmetic(maybeVariant.get()));
                }
            }
        }
        return Optional.of(new EquippedCosmetic(cosmetic));
    }
     
}
