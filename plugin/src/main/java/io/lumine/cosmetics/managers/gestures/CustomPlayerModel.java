package io.lumine.cosmetics.managers.gestures;

import com.ticxo.playeranimator.api.model.player.PlayerModel;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CustomPlayerModel extends PlayerModel {

	@Getter private final QuitMethod quitMethod;
	private final boolean canLook;
	private final Runnable onEnd;
	private float lockedYaw;
	private boolean isPlaying;

	public CustomPlayerModel(Player player, QuitMethod quitMethod, boolean canLook, Runnable onEnd) {
		super(player);
		this.quitMethod = quitMethod;
		this.canLook = canLook;
		if(!canLook)
			lockedYaw = player.getLocation().getYaw();
		this.onEnd = onEnd;
	}

	public void stopAnimation(QuitMethod method) {
		isPlaying = method == null || quitMethod != method;
	}

	@Override
	public void playAnimation(String name) {
		super.playAnimation(name);
		isPlaying = true;
	}

	@Override
	public boolean update() {
		boolean update = super.update();
		if(!update || !isPlaying) {
			onEnd.run();
			return false;
		}
		return true;
	}

	@Override
	public float getBaseYaw() {
		if(canLook)
			return super.getBaseYaw();
		return lockedYaw;
	}

}
