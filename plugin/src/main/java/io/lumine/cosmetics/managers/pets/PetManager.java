package io.lumine.cosmetics.managers.pets;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.MCCosmeticsManager;

import java.io.File;

public class PetManager extends MCCosmeticsManager<Pet> {

	public PetManager(MCCosmeticsPlugin plugin) {
		super(plugin, Pet.class);
	}

	@Override
	public Pet build(File file, String node) {
		return new Pet(this, file, node);
	}

	@Override
	public void equip(CosmeticProfile profile) {
		var maybePet = profile.getEquipped(Pet.class);
		
		if(maybePet.isEmpty()) {
		    return;
		}
		
		var pet = (Pet) maybePet.get().getCosmetic();
		
		if(pet.getSpawner().isValid()) {
		    pet.getSpawner().equip(profile.getPlayer());
		}
	}

	@Override
	public void unequip(CosmeticProfile profile) {

	}
}
