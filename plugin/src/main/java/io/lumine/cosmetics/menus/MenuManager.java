package io.lumine.cosmetics.menus;

import java.util.List;

import com.google.common.collect.Lists;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

public class MenuManager extends ReloadableModule<MCCosmeticsPlugin> {

    @Getter private CustomizeMenu customizeMenu;
    @Getter private GenericSelectionMenu selectionMenu;

    public MenuManager(MCCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {

        if(customizeMenu == null) {
            customizeMenu = new CustomizeMenu(plugin,this);
            selectionMenu = new GenericSelectionMenu(plugin,this);
        }
        
        customizeMenu.reload();
        selectionMenu.reload();
    }

    @Override
    public void unload() {}

    public void openCosmeticMenu(MCCosmeticsManager manager, Profile profile) {
        // TODO: sorting
        var cosmetics = Lists.newArrayList(manager.getAllCosmetics());

        if(manager.getMenu() == null) {
            getSelectionMenu().open(profile.getPlayer(), new SelectionMenuContext(profile, manager), cosmetics);
        } else {
            manager.getMenu().open(profile.getPlayer(), profile, cosmetics);
        }
    }

}
