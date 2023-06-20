package io.lumine.cosmetics.managers.modelengine;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.AnimationProperty;
import com.ticxo.modelengine.api.animation.blueprint.LoopMode;
import com.ticxo.modelengine.api.animation.property.SimpleProperty;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.nms.entity.wrapper.RangeManager;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.wardrobe.WardrobeMegDummy;
import io.lumine.utils.Events;
import io.lumine.utils.Schedulers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;

public class MEGManager extends MCCosmeticsManager<MEGAccessory> {

	public MEGManager(MCCosmeticsPlugin plugin) {
		super(plugin, MEGAccessory.class);

		load(plugin);
	}

	@Override
	public void load(MCCosmeticsPlugin plugin) {
		super.load(plugin);

		Events.subscribe(PlayerDeathEvent.class)
            .handler(event -> {
                final var player = event.getEntity();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile != null && profile.getEquipped(MEGAccessory.class).isPresent()) {
                    unequip(profile);
                }
            })
            .bindWith(this);

        Events.subscribe(PlayerRespawnEvent.class)
            .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                Schedulers.sync().runLater(() -> {
                    if(profile != null && profile.getEquipped(MEGAccessory.class).isPresent()) {
                        equip(profile);
                    }
                }, 5);
            })
            .bindWith(this);
		
		Events.subscribe(PlayerChangedWorldEvent.class)
		    .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile.getEquipped(MEGAccessory.class).isPresent()) {
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
		
		Events.subscribe(PlayerTeleportEvent.class)
            .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile.getEquipped(MEGAccessory.class).isPresent()) {
                    unequip(profile);
                    
                    Schedulers.sync().runLater(() -> {
                        equip(profile);
                    }, 5);
                }
            })
            .bindWith(this);
		
		Events.subscribe(PlayerQuitEvent.class)
			.handler(event -> {
				final Player player = event.getPlayer();
				getProfiles().awaitProfile(player).thenAcceptAsync(maybeProfile -> {
					if(maybeProfile.isEmpty()) {
						return;
					}
					unequip(maybeProfile.get());
				});
			}).bindWith(this);
	}
	
	@Override
	public void unload() {
	    getPlugin().getProfiles().getKnownProfiles().forEach(this::unequip);
	    super.unload();
	}

	@Override
	public MEGAccessory build(File file, String node) {
		return new MEGAccessory(this, file, node);
	}

	@Override
	public void equip(CosmeticProfile profile) {
		final var maybeEquipped = profile.getEquipped(MEGAccessory.class);
		if(maybeEquipped.isEmpty()) {
		    return;
		}
		var opt = maybeEquipped.get().getCosmetic();
		
		if(!(opt instanceof MEGAccessory meg)) {
			return;
		}

		final var blueprint = ModelEngineAPI.getBlueprint(meg.getModelId());
		if(blueprint == null)
			return;

		final var animation = blueprint.getAnimations().get(meg.getState());
		if(animation == null)
			return;

		final var player = profile.getPlayer();
		final var activeModel = ModelEngineAPI.createActiveModel(blueprint);
		final var property = new SimpleProperty(activeModel, animation, 1, 0, 1);
		property.setForceLoopMode(LoopMode.LOOP);
		property.setForceOverride(true);
		activeModel.getAnimationHandler().playAnimation(property, true);
		activeModel.setCanHurt(false);

		ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(player.getUniqueId());
		if(modeledEntity == null) {
			modeledEntity = ModelEngineAPI.createModeledEntity(new FakeEntity(player, meg.getOffset(), meg.getAnchor()));
			modeledEntity.setBaseEntityVisible(true);
			ModelEngineAPI.getEntityHandler().setSelfFakeInvisible(player, false);
			if(modeledEntity.getRangeManager() instanceof RangeManager.Disguise disguise)
				disguise.setIncludeSelf(true);
		}

		modeledEntity.destroy();
		modeledEntity.addModel(activeModel, false);
	}

	public void unequip(CosmeticProfile profile) {
        final var maybeEquipped = profile.getEquipped(MEGAccessory.class);
        if(maybeEquipped.isEmpty()) {
            return;
        }
        var opt = maybeEquipped.get().getCosmetic();
        
        if(!(opt instanceof MEGAccessory)) {
            return;
        }

		final var player = profile.getPlayer();
		ModelEngineAPI.removeModeledEntity(player.getUniqueId());
	}
	
    @Override
    public void equipMannequin(Mannequin mannequin, EquippedCosmetic cosmetic) {

        var opt = cosmetic.getCosmetic();
        
        if(!(opt instanceof MEGAccessory meg)) {
            return;
        }

        final var blueprint = ModelEngineAPI.getBlueprint(meg.getModelId());
        if(blueprint == null)
            return;

        final var animation = blueprint.getAnimations().get(meg.getState());
        if(animation == null)
            return;
        
        mannequin.setCleanupWhenDone(MEGAccessory.class);

        final var activeModel = ModelEngineAPI.createActiveModel(blueprint);
        final var property = new SimpleProperty(activeModel, animation, 1, 0, 1);
        property.setForceLoopMode(LoopMode.LOOP);
        property.setForceOverride(true);
        activeModel.getAnimationHandler().playAnimation(property, true);
        activeModel.setCanHurt(false);

        var dummy = new WardrobeMegDummy(mannequin, meg.getOffset(), meg.getAnchor());
        
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(mannequin.getUniqueId());
        if(modeledEntity == null) {
            modeledEntity = ModelEngineAPI.createModeledEntity(dummy);
            modeledEntity.setBaseEntityVisible(true);
            if(modeledEntity.getRangeManager() instanceof RangeManager.Disguise disguise) {
                disguise.setIncludeSelf(true);
            }

            modeledEntity.getRangeManager().forceSpawn(mannequin.getPlayer());
        }

        modeledEntity.destroy();
        modeledEntity.addModel(activeModel, false);
    }
    
    @Override
    public void unequipMannequin(Mannequin mannequin) {
        ModelEngineAPI.removeModeledEntity(mannequin.getUniqueId());
    }

}
