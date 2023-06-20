package io.lumine.cosmetics.players.wardrobe;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Maps;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.api.players.wardrobe.WardrobeManager;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.utils.Events;
import io.lumine.utils.plugin.ReloadableModule;

public class WardrobeExecutor extends ReloadableModule<MCCosmeticsPlugin> implements WardrobeManager  {

    private Map<Player,WardrobeTrackerImpl> usingWardrobe = Maps.newConcurrentMap();
    
    public WardrobeExecutor(MCCosmeticsPlugin plugin) {
        super(plugin, false);

    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {


    }

    @Override
    public void unload() {

    }
    
    public WardrobeTrackerImpl createTracker(Player player) {
        return new WardrobeTrackerImpl(this, player);
    }
    
    public void openWardrobe(Player player) {
        closeWardrobe(player);
        
        var tracker = createTracker(player);
        
        usingWardrobe.put(player,tracker);
        
        CommandHelper.sendEditorMessage(player, new String[] {
                "<green>You're now in Wardrobe Mode!",
                "Cosmetics equipped will go on your mannequin",
                "To exit, type the command again or walk away"
        });
    }
    
    public void closeWardrobe(Player player) {
        var tracker = usingWardrobe.remove(player);
        
        if(tracker != null) {
            tracker.terminate();
            CommandHelper.sendSuccess(player, "You exited Wardrobe Mode");
        }
    }

    public boolean isInWardrobe(Player player) {
        return usingWardrobe.containsKey(player);
    }

    public Mannequin getMannequin(Player player) {
        return usingWardrobe.get(player).getMannequin();
    }
    

}
