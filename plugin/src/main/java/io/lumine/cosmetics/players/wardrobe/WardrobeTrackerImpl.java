package io.lumine.cosmetics.players.wardrobe;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Sets;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.api.players.wardrobe.WardrobeManager;
import io.lumine.cosmetics.api.players.wardrobe.WardrobeTracker;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.utils.Events;
import io.lumine.utils.Players;
import io.lumine.utils.Schedulers;
import io.lumine.utils.particles.Particle;
import io.lumine.utils.plugin.PluginComponent;
import lombok.Getter;

public class WardrobeTrackerImpl extends PluginComponent<MCCosmeticsPlugin> implements WardrobeTracker,Runnable {

    private static final int DESPAWN_RADIUS = 8*8;
    
    @Getter private final WardrobeManager manager;
    @Getter private final Player player;
    private final Location location;
    
    @Getter private Mannequin mannequin;
    private Collection<Class<? extends Cosmetic>> cleanup = Sets.newConcurrentHashSet();
    
    public WardrobeTrackerImpl(WardrobeExecutor manager, Player player) {
        super(manager.getPlugin());
        
        this.manager = manager;
        this.player = player;
        this.location = player.getLocation();
        
        location.setPitch(0);
        location.add(location.getDirection().normalize().multiply(4));
        
        Events.subscribe(PlayerQuitEvent.class)
            .filter(event -> event.getPlayer().equals(player))
            .handler(event -> this.terminate())
            .bindWith(this);
        
        Schedulers.async().runRepeating(this, 20, 20).bindWith(this);
        
        this.mannequin = manager.getPlugin().getVolatileCodeHandler().createMannequin(this, player, location);
        
        Players.playSound(player, location, Sound.BLOCK_CONDUIT_ACTIVATE);
        Particle.CLOUD.create()
            .at(location.clone().add(0,1,0))
            .amount(100)
            .offset(0.5F, 1F, 0.5F)
            .speed(0.1F)
            .send(player);
    }

    public void setCleanupWhenDone(Class<? extends Cosmetic> type) {
        cleanup.add(type);
    }
    
    @Override
    public void run() {
        if(player.getLocation().distanceSquared(location) > DESPAWN_RADIUS) {
            this.terminate();
        }
    }

    @Override
    public boolean terminate() {
        if(this.mannequin != null) {
            for(var clazz : cleanup) {
                var type = CosmeticType.get(clazz).type();
                getPlugin().getCosmetics().getManager(type).ifPresent(manager -> {
                    manager.unequipMannequin(mannequin);
                });
            }
            this.mannequin.despawn();
            this.mannequin = null;
            
            manager.closeWardrobe(player);
            
            Players.playSound(player, location, Sound.BLOCK_CONDUIT_DEACTIVATE);
            Particle.ENCHANTMENT_TABLE.create()
                .at(location.clone().add(0,1,0))
                .amount(200)
                .offset(0.5F, 1F, 0.5F)
                .speed(0.1F)
                .send(player);
        }
        return super.terminate();
    }
}
