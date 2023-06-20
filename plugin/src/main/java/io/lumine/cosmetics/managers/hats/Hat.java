package io.lumine.cosmetics.managers.hats;

import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.items.ItemFactory;
import io.lumine.utils.menu.Icon;

import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Hat extends AbstractCosmetic implements ColorableCosmetic,ItemCosmetic {

	public Hat(HatManager manager, File file, String key) {
	    super(manager, file, CosmeticType.type(Hat.class), key);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}


    @Override
    public Icon<CosmeticProfile> getIcon() {
        return buildIcon("hat");
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
