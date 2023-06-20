package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class CustomizeMenu extends CosmeticMenu<Profile> {

    public CustomizeMenu(MCCosmeticsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/customize", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        for(var entry : getPlugin().getCosmetics().getCosmeticManagers().entrySet()) {
            final var manager = entry.getValue();
            final var type = CosmeticType.folder(manager.getCosmeticClass());

            builder.getIcon("BUTTON_" + type.toUpperCase()).ifPresent(icon -> {
                icon.getBuilder().click((profile,player) -> {
                    getMenuManager().openCosmeticMenu(manager,profile);
                });
            });
        }
        
        return builder;
    }

}
