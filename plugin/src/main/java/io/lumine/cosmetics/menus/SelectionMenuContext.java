package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;
import lombok.Data;

@Data
public class SelectionMenuContext {

    private final Profile profile;
    private final MCCosmeticsManager manager;

}
