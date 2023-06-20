package io.lumine.cosmetics.managers.particle;

import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;

import java.io.File;

public class ParticleAccessory extends AbstractCosmetic implements ColorableCosmetic {

	public ParticleAccessory(ParticleManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(ParticleAccessory.class), key);
	}

	@Override
	public String getPropertyNode() {
		return key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("particle effects");
	}
}
