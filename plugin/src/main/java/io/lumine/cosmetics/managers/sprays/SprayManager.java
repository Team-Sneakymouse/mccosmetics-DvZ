package io.lumine.cosmetics.managers.sprays;

import io.lumine.utils.Events;
import io.lumine.utils.Schedulers;
import io.lumine.utils.config.properties.types.IntProp;
import io.lumine.utils.cooldown.Cooldown;
import io.lumine.utils.cooldown.CooldownMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.managers.back.BackAccessory;
import io.lumine.cosmetics.nms.cosmetic.VolatileSprayHelper;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.BooleanProp;
import io.lumine.utils.config.properties.types.DoubleProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.files.Files;
import io.lumine.utils.logging.Log;
import io.lumine.utils.numbers.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.RayTraceResult;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static io.lumine.cosmetics.compat.WorldGuardSupport.SPRAY_FLAG;

public class SprayManager extends MCCosmeticsManager<Spray> {

    private static final StringProp SPRAY_SOUND = Property.String(Scope.CONFIG, "Configuration.Sprays.Sound", "entity.cat.hiss");
    private static final DoubleProp SPRAY_SOUND_VOL = Property.Double(Scope.CONFIG, "Configuration.Sprays.Volume", 1D);
    private static final DoubleProp SPRAY_SOUND_PI = Property.Double(Scope.CONFIG, "Configuration.Sprays.Pitch", 2D);

    private static final BooleanProp REPLACE_SPRAY = Property.Boolean(Scope.CONFIG, "Configuration.Sprays.ReplaceLast", true);
    private static final IntProp SPRAY_PERSIST = Property.Int(Scope.CONFIG, "Configuration.Sprays.PersistTime", 60);
    
    private final static CooldownMap<UUID> keytapTimer = CooldownMap.create(Cooldown.of(500, TimeUnit.MILLISECONDS));
    
    private final AtomicInteger currentMapId = new AtomicInteger(Integer.MIN_VALUE);
    
    private final Map<String,SprayImage> images = Maps.newConcurrentMap();

    private final Map<UUID,Integer> activeByPlayer = new ConcurrentHashMap<>(); 

    public SprayManager(MCCosmeticsPlugin plugin) {
        super(plugin, Spray.class);   
        
        load(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        loadSprayImages();
        
        super.load(plugin);
        
        Events.subscribe(PlayerChangedWorldEvent.class)
            .handler(event -> {
                final var player = event.getPlayer();
                removeSpray(player);
            }).bindWith(this);
        
        Events.subscribe(PlayerQuitEvent.class)
            .handler(event -> {
                removeSpray(event.getPlayer());
            }).bindWith(this);
        
        Events.subscribe(PlayerSwapHandItemsEvent.class)
            .filter(event -> event.getPlayer().isSneaking())
            .handler(event -> {
                if(!keytapTimer.test(event.getPlayer().getUniqueId())) {
                    useSpray(event.getPlayer());
                }
            }).bindWith(this);
    }

    @Override
    public Spray build(File file, String node) {
        return new Spray(this, file, node);
    }

    @Override
    public void equip(CosmeticProfile profile) {}

    @Override
    public void unequip(CosmeticProfile profile) {
        removeSpray(profile.getPlayer());
    }

    private void loadSprayImages() {
        images.clear();
        
        final Collection<File> files = Lists.newArrayList();
        final String type = CosmeticType.folder(cosmeticClass);
        for(var packFolder : plugin.getConfiguration().getPackFolders()) {
            final File confFolder = new File(packFolder.getAbsolutePath() + System.getProperty("file.separator") + type);
            if(confFolder.exists() && confFolder.isDirectory()) {
                files.addAll(Files.getAll(confFolder.getAbsolutePath(), Lists.newArrayList("png", "jpg")));
            }
        }
        
        for(var file : files) {
            var img = createSprayImage(file);
            images.put(img.getName(), img);
        }

        Log.info("Loaded " + images.size() + " spray images");
    }
    
    private SprayImage createSprayImage(File file) {
        return new SprayImage(this, file, getNextMapId());
    }

    private int getNextMapId() {
        return currentMapId.updateAndGet(id -> (id + 1));
    }
    
    public SprayImage getSprayImage(String name) {
        return images.getOrDefault(name, null);
    }
    
    public boolean useSpray(Player player) {
        var profile = getPlugin().getProfiles().getProfile(player);
        var maybeSpray = profile.getEquipped(Spray.class);
        
        if(maybeSpray.isPresent()) {
            return useSpray(player, (Spray) maybeSpray.get().getCosmetic());
        } else {
            return false;
        }
    }

    
    public boolean useSpray(Player player, Spray spray) {
        final Location location = player.getEyeLocation();
        if(plugin.getWorldGuardSupport() != null) {
            if (!plugin.getWorldGuardSupport().canSprayWG(player, location)) {
                return false;
            }
        }
        
        RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10, FluidCollisionMode.ALWAYS, false, 1, (entity) -> {
            return false;
        });
        
        if(result == null) {
            return false;
        }
            
        if(result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME) {
            return false;
        }
        
        final int rotation;
        if(result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
            rotation = getRotation(player.getLocation().getYaw(), false) * 45;
        } else {
            rotation = 0;
        }
        
        Location framelocation = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
        spawnSpray(player, spray, framelocation, result.getHitBlockFace(), rotation);
        return true;
    }
    
    public void spawnSpray(Player player, Spray spray, Location location, BlockFace face, int rotation) {       
        if(REPLACE_SPRAY.get()) {
            removeSpray(player);
        }
        
        int eid = ((VolatileSprayHelper) getNMSHelper()).drawSpray(spray, location, face, rotation);
                
        if(REPLACE_SPRAY.get()) {
            activeByPlayer.put(player.getUniqueId(), eid);
        }

        final var sound = SPRAY_SOUND.get();
        final double volume = SPRAY_SOUND_VOL.get();
        final double pitch = SPRAY_SOUND_PI.get();

        location.getWorld().getPlayers().forEach(p -> p.playSound(location, sound, (float) volume, (float) pitch));
         
        Schedulers.sync().runLater(() -> {
            location.getWorld().getPlayers().forEach(p -> p.playSound(location, sound, (float) volume, (float) pitch));
        }, 2);
        Schedulers.sync().runLater(() -> {
            location.getWorld().getPlayers().forEach(p -> p.playSound(location, sound, (float) volume, (float) pitch));
        }, 4);

        if(SPRAY_PERSIST.get() > 0) {
            Schedulers.sync().runLater(() -> {
                removeSpray(eid);
            }, SPRAY_PERSIST.get() * 20);
        }
    }
    
    public void removeSpray(Player player) {
        if(activeByPlayer.containsKey(player.getUniqueId())) {
            int oid = activeByPlayer.get(player.getUniqueId());
            getPlugin().getVolatileCodeHandler().removeFakeEntity(oid);
        }
    }
    
    public void removeSpray(int id) {
        getPlugin().getVolatileCodeHandler().removeFakeEntity(id);
    }
    
    private int getRotation(float yaw, boolean allowDiagonals) {
        if(allowDiagonals) {
            return Numbers.floor(((Location.normalizeYaw(yaw) + 180) * 8 / 360) + 0.5F) % 8;
        } else {
            return Numbers.floor(((Location.normalizeYaw(yaw) + 180) * 4 / 360) + 0.5F) % 4;
        }
    }
}
