package io.lumine.cosmetics.managers.particle;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.MCCosmeticsManager;

import java.io.File;

public class ParticleManager extends MCCosmeticsManager<ParticleAccessory> {

	public ParticleManager(MCCosmeticsPlugin plugin, Class<ParticleAccessory> particleAccessoryClass) {
		super(plugin, particleAccessoryClass);
	}

	@Override
	public ParticleAccessory build(File file, String node) {
		return new ParticleAccessory(this, file, node);
	}

	@Override
	public void equip(CosmeticProfile profile) {
		// TODO: 15/3/2022 Impl particle accessory
	}

	@Override
	public void unequip(CosmeticProfile profile) {

	}
}
