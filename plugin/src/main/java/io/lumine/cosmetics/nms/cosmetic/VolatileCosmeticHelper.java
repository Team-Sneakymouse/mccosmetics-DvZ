package io.lumine.cosmetics.nms.cosmetic;

import org.bukkit.entity.Player;

import java.util.List;

public interface VolatileCosmeticHelper {

	default boolean read(Player sender, Object packet, boolean isCanceled) {
		return true;
	}

	default List<Object> write(Player receiver, Object packet) {
		return null;
	}

}
