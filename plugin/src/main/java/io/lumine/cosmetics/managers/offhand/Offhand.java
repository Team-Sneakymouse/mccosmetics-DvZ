package io.lumine.cosmetics.managers.offhand;

import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.items.ItemFactory;
import io.lumine.utils.menu.Icon;

import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Offhand extends AbstractCosmetic implements ColorableCosmetic,ItemCosmetic {

	public Offhand(OffhandManager manager, File file, String key) {
	    super(manager, file, CosmeticType.type(Offhand.class), key);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

    @Override
    public Icon<CosmeticProfile> getIcon() {
        return buildIcon("offhand");
    }

    @Override
    public ItemStack getCosmetic(EquippedCosmetic equipped) {
        var item = getMenuItem();
        
        if(equipped.getVariant() != null) {
            var variant = equipped.getVariant();
            item = ItemFactory.of(item).color(variant.getColor()).build();
        } else if(equipped.getColor() != null) {
            item = ItemFactory.of(item).color(equipped.getColor()).build();
        }

        return item;
    }
}
