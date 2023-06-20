package io.lumine.cosmetics.api.cosmetics;

import java.util.Optional;

import io.lumine.cosmetics.api.cosmetics.manager.CosmeticManager;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.menu.MenuData;
import lombok.Getter;

public abstract class Cosmetic implements PropertyHolder,MenuData<CosmeticProfile> {

    @Getter private final CosmeticManager manager;
    @Getter private final String type;
    @Getter private final String key;
    
    public Cosmetic(CosmeticManager manager, String type, String key) {
        this.manager = manager;
        this.type = type;
        this.key = key;
    }

    public boolean has(CosmeticProfile profile) {
        return profile.has(this);
    }
    
    public void equip(CosmeticProfile profile) {
        profile.equip(this);
    }

    public boolean isEquipped(CosmeticProfile profile) {
        var maybeCosmetic = profile.getEquipped(getClass());

        return maybeCosmetic.isPresent() && maybeCosmetic.get().equals(this);
    }
    
    public abstract String getId();
    
    public abstract String getNamespace();
    
    public abstract String getPermission();
    
    public abstract boolean hasVariants();
    
    public abstract Optional<CosmeticVariant> getVariant(String id);
    
}
