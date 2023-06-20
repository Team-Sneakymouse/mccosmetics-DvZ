package io.lumine.cosmetics.api.cosmetics;

import io.lumine.cosmetics.api.cosmetics.manager.CosmeticManager;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.menu.MenuData;
import lombok.Getter;

public abstract class CosmeticVariant implements PropertyHolder,MenuData<CosmeticProfile> {

    @Getter private final Cosmetic cosmetic;
    @Getter private final String key;
    
    public CosmeticVariant(Cosmetic cosmetic, String key) {
        this.cosmetic = cosmetic;
        this.key = key;
    }
    
    public abstract String getColor();
}
