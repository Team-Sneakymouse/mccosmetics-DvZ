package io.lumine.cosmetics.managers.back;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.cosmetics.manager.HideableCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.managers.gestures.Gesture;
import io.lumine.cosmetics.managers.modelengine.MEGAccessory;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.lumine.utils.Events;
import io.lumine.utils.Schedulers;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackManager extends MCCosmeticsManager<BackAccessory> implements HideableCosmetic {

    public BackManager(MCCosmeticsPlugin plugin) {
        super(plugin, BackAccessory.class);

        load(plugin);
    }
    
    public void load(MCCosmeticsPlugin plugin) {
        super.load(plugin);
        
        Events.subscribe(PlayerChangedWorldEvent.class)
            .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile != null && profile.getEquipped(BackAccessory.class).isPresent()) {
                    unequip(profile);
                    
                    Schedulers.sync().runLater(() -> {
                        equip(profile);
                    }, 5);
                }
            })
            .bindWith(this);
        
        Events.subscribe(PlayerTeleportEvent.class)
            .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile != null && profile.getEquipped(BackAccessory.class).isPresent()) {
                    unequip(profile);
                    
                    Schedulers.sync().runLater(() -> {
                        equip(profile);
                    }, 5);
                }
            })
            .bindWith(this);
        
        Events.subscribe(PlayerGameModeChangeEvent.class)
            .handler(event -> {
                if(event.getNewGameMode() == GameMode.SPECTATOR) {
                    final var player = event.getPlayer();
                    final var profile = plugin.getProfiles().getProfile(player);
                    
                    unequip(profile);
                } else if(event.getPlayer().getPlayer().getGameMode() == GameMode.SPECTATOR) {
                    final var player = event.getPlayer();
                    final var profile = plugin.getProfiles().getProfile(player);
                    
                    Schedulers.sync().runLater(() -> {
                        if(player.getGameMode() != GameMode.SPECTATOR) {
                            equip(profile);
                        }
                    }, 5);
                }
            })
            .bindWith(this);
    }

    @Override
    public BackAccessory build(File file, String node) {
        return new BackAccessory(this, file, node);
    }

    @Override
    public void equip(CosmeticProfile profile) {
        getHelper().apply(profile);
    }

    @Override
    public void unequip(CosmeticProfile profile) {
        getHelper().unapply(profile);
    }
    
    @Override
    public void equipMannequin(Mannequin mannequin, EquippedCosmetic cosmetic) {
        getHelper().equipMannequin(mannequin, cosmetic);
    }
    
    @Override
    public void unequipMannequin(Mannequin mannequin) {
        getHelper().unequipMannequin(mannequin);
    }

    @Override
    public void hide(CosmeticProfile profile, Cosmetic request) {
        if(!(request instanceof Gesture))
            return;
        profile.setHidden(getCosmeticClass(), true);
        getHelper().unapply(profile);
    }

    @Override
    public void show(CosmeticProfile profile) {
        if(profile.getPlayer().isDead())
            return;
        profile.setHidden(getCosmeticClass(), false);
        getHelper().apply(profile);
    }

    protected VolatileEquipmentHelper getHelper() {
        return (VolatileEquipmentHelper) getNMSHelper();
    }

}
