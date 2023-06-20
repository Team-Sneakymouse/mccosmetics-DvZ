package io.lumine.cosmetics.managers.chat;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;
import java.io.File;

import org.bukkit.entity.Player;

import lombok.Getter;

public class ChatTag extends AbstractCosmetic {

	public ChatTag(ChatTagManager manager, File file, String key) {
	    super(manager, file, CosmeticType.type(ChatTag.class), key);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

    @Override
    public Icon<CosmeticProfile> getIcon() {
        return buildIcon("chattag");
    }

}
