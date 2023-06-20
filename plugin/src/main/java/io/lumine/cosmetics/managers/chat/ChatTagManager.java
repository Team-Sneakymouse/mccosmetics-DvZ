package io.lumine.cosmetics.managers.chat;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.managers.sprays.Spray;
import java.io.File;

public class ChatTagManager extends MCCosmeticsManager<ChatTag> {

    public ChatTagManager(MCCosmeticsPlugin plugin) {
        super(plugin, ChatTag.class);   
        
        load(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {

        super.load(plugin);

    }

    @Override
    public ChatTag build(File file, String node) {
        return null;
    }

    @Override
    public void equip(CosmeticProfile profile) {}

    @Override
    public void unequip(CosmeticProfile profile) {

    }


}
