package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.Schedulers;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class SelectionMenu extends CosmeticMenu<Profile> {

    private final MCCosmeticsManager cosmeticManager;
    
    public SelectionMenu(MCCosmeticsPlugin core, MenuManager manager, MCCosmeticsManager cmanager, String type) {
        super(core, manager, new MenuProp(core, "menus/selection_" + type.toLowerCase(), "Menu", null));

        this.cosmeticManager = cmanager;
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {
        builder = addPageButtons(builder);
        
        builder.getIcon("BUTTON_BACK").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                playMenuClick(player);
                getPlugin().getMenuManager().getCustomizeMenu().open(player, profile);
            });
        });
        
        builder.getIcon("BUTTON_REMOVE").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                playMenuClick(player);
                cosmeticManager.unequip(profile);
                profile.unequip(cosmeticManager.getCosmeticClass());
                if(Property.Boolean(Scope.CONFIG, "Configuration.Menus.Close-On-Unequip", false).get()) {
                    player.closeInventory();
                }
                
                Schedulers.sync().runLater(() -> {
                    cosmeticManager.unequip(profile);
                }, 5);
            });
        });
        
        return builder;
    }

}
