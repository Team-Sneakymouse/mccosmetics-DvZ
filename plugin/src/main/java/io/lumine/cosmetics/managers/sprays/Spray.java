package io.lumine.cosmetics.managers.sprays;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;
import java.io.File;

import org.bukkit.entity.Player;

import lombok.Getter;

public class Spray extends AbstractCosmetic {

    @Getter private final SprayImage image;
    
	public Spray(SprayManager manager, File file, String key) {
	    super(manager, file, CosmeticType.type(Spray.class), key);

	    this.image = manager.getSprayImage(key);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

    @Override
    public Icon<CosmeticProfile> getIcon() {
        return buildIcon("spray");
    }
    
    public void use(Player player) {
        ((SprayManager) getManager()).useSpray(player, this);
    }

}
