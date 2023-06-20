package io.lumine.cosmetics.managers;

import java.io.File;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.LangListProp;
import io.lumine.utils.config.properties.types.LangProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import lombok.Getter;

public class AbstractCosmeticVariant extends CosmeticVariant {

    protected static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
    protected static final LangListProp DESCRIPTION = Property.LangList(Scope.NONE, "Description");
    protected static final StringProp COLOR = Property.String(Scope.NONE, "Color");
    
    @Getter private final String propertyNode;
    
    @Getter private final String color;
    
    public AbstractCosmeticVariant(Cosmetic cosmetic, File file, String key) {
        super(cosmetic, key);
        
        this.propertyNode = getCosmetic().getPropertyNode() + ".Variants." + key;
        this.color = COLOR.fget(file, this);
    }

    @Override
    public Icon<CosmeticProfile> getIcon() {
        return null;
    }
    

}
