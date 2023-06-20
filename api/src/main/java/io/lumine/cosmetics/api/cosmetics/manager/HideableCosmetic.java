package io.lumine.cosmetics.api.cosmetics.manager;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;

public interface HideableCosmetic {

	void hide(CosmeticProfile profile, Cosmetic request);
	void show(CosmeticProfile profile);

}
