package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.Schedulers;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class GenericSelectionMenu extends CosmeticMenu<SelectionMenuContext> {

    public GenericSelectionMenu(MCCosmeticsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/selection", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<SelectionMenuContext> build(EditableMenuBuilder<SelectionMenuContext> builder) {
        builder.getIcon("NEXT_PAGE").ifPresent(icon -> {
            icon.getBuilder().click((context,player) -> {
                playMenuClick(player);
                nextPage(player);
            });
        });
        
        builder.getIcon("PREVIOUS_PAGE").ifPresent(icon -> {
            icon.getBuilder().click((context,player) -> {
                playMenuClick(player);
                previousPage(player);
            });
        });
        
        builder.getIcon("BUTTON_BACK").ifPresent(icon -> {
            icon.getBuilder().click((context,player) -> {
                playMenuClick(player);
                getMenuManager().getCustomizeMenu().open(player, context.getProfile());
            });
        });
        
        builder.getIcon("BUTTON_REMOVE").ifPresent(icon -> {
            icon.getBuilder().click((context,player) -> {
                playMenuClick(player);
                context.getManager().unequip(context.getProfile());
                context.getProfile().unequip(context.getManager().getCosmeticClass());
                player.closeInventory();
                
                Schedulers.sync().runLater(() -> {
                    context.getManager().unequip(context.getProfile());
                }, 5);
            });
        });
        
        return builder;
    }

}
