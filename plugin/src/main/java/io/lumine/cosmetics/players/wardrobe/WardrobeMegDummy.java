package io.lumine.cosmetics.players.wardrobe;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.nms.entity.wrapper.BodyRotationController;
import com.ticxo.modelengine.api.utils.math.Offset;

import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.managers.modelengine.ModelAnchor;
import io.lumine.utils.serialize.Orient;

public class WardrobeMegDummy extends Dummy {

    private final Mannequin mannequin;
    private final Vector offset;
    private final double yaw;
    private final double pitch;
    private final ModelAnchor anchor;
    private BodyRotationController controller;

    public WardrobeMegDummy(Mannequin mannequin, Orient orient, ModelAnchor anchor) {
        super(mannequin.getEntityId(), mannequin.getUniqueId());
        this.mannequin = mannequin;
        
        offset = orient.getLocus().toVector();
        yaw = Math.toRadians(orient.getDirection().getYaw());
        pitch = Math.toRadians(orient.getDirection().getPitch());
        this.anchor = anchor;
        
        this.setYBodyRot(mannequin.getRotation());
        this.setYHeadRot(mannequin.getRotation());
    }

    @Override
    public BodyRotationController wrapBodyRotationControl() {
        controller = super.wrapBodyRotationControl();
        controller.setMaxHeadAngle(45);
        controller.setMaxBodyAngle(45);
        controller.setStableAngle(5);
        return controller;
    }

    @Override
    public Location getLocation() {
        Location location = mannequin.getLocation().clone();
        Vector offset;
        if (anchor == ModelAnchor.HEAD) {
            double pYaw = Math.toRadians(location.getYaw());
            double pPitch = Math.toRadians(location.getPitch());
            offset = Offset.rotateYaw(Offset.rotatePitch(this.offset.clone(), pitch + pPitch), yaw + pYaw);
        }else {
            double pYaw = Math.toRadians(getYBodyRot());
            offset = Offset.rotateYaw(this.offset.clone(), yaw + pYaw);
        }
        return location.add(offset);
    }
}
