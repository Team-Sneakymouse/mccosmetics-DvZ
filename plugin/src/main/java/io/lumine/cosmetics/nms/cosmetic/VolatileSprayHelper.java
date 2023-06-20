package io.lumine.cosmetics.nms.cosmetic;

import io.lumine.cosmetics.managers.sprays.Spray;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public interface VolatileSprayHelper extends VolatileCosmeticHelper {

    int drawSpray(Spray spray, Location location, BlockFace face, int rotation);

}
