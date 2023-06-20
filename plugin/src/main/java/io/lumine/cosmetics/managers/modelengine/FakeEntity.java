package io.lumine.cosmetics.managers.modelengine;

import com.ticxo.modelengine.api.entity.BukkitPlayer;
import com.ticxo.modelengine.api.nms.entity.wrapper.BodyRotationController;
import com.ticxo.modelengine.api.utils.math.Offset;
import io.lumine.utils.serialize.Orient;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class FakeEntity extends BukkitPlayer {

	private final Vector offset;
	private final double yaw;
	private final double pitch;
	private final ModelAnchor anchor;
	private BodyRotationController controller;

	public FakeEntity(@NotNull Player entity, Orient orient, ModelAnchor anchor) {
		super(entity);
		offset = orient.getLocus().toVector();
		yaw = Math.toRadians(orient.getDirection().getYaw());
		pitch = Math.toRadians(orient.getDirection().getPitch());
		this.anchor = anchor;
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
		Location location = getOriginal().getLocation();
		Vector offset;
		if (anchor == ModelAnchor.HEAD) {
			double pYaw = Math.toRadians(location.getYaw());
			double pPitch = Math.toRadians(location.getPitch());
			offset = Offset.rotateYaw(Offset.rotatePitch(this.offset.clone(), pitch + pPitch), yaw + pYaw);
		}else {
			double pYaw = Math.toRadians(controller == null ? getYBodyRot() : controller.getYBodyRot());
			offset = Offset.rotateYaw(this.offset.clone(), yaw + pYaw);
		}
		return location.add(offset);
	}
}
