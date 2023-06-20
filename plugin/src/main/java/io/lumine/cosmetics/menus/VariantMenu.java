package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class VariantMenu extends CosmeticMenu<Profile> {

    public VariantMenu(MCCosmeticsPlugin core, MenuManager manager, String type) {
        super(core, manager, new MenuProp(core, "menus/variant_selection_" + type.toLowerCase(), "Menu", null));
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {
        return builder;
    }

}
