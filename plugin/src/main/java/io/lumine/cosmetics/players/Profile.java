package io.lumine.cosmetics.players;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.storage.sql.SqlStorage;
import io.lumine.cosmetics.storage.sql.mappings.Keys;
import io.lumine.cosmetics.storage.sql.mappings.tables.records.ProfileRecord;
import io.lumine.utils.Schedulers;
import io.lumine.utils.serialize.Chroma;
import lombok.Getter;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Profile implements CosmeticProfile,io.lumine.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    @Getter private transient final Map<Class<? extends Cosmetic>, EquippedCosmetic> equipped = Maps.newConcurrentMap();
    @Getter private transient final Set<Class<? extends Cosmetic>> hidden = Sets.newConcurrentHashSet();
    @Getter private Map<String,ProfileCosmeticData> equippedCosmetics = Maps.newConcurrentMap();
        
    @Getter private transient ProfileManager manager;
    @Getter private transient Player player;
    
    public Profile() {}
    
    public Profile(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public void initialize(final ProfileManager manager, final Player player)  {
        this.manager = manager;
        this.player = player;
        
        reloadCosmetics();
    }
    
    public void loadFromSql(ProfileRecord record) {
        var fetchEquipped = record.fetchChildren(Keys.MCCOSMETICS_PROFILE_EQUIPPED_PROFILE_FK);
        
        for(var equipRecord : fetchEquipped) {
            equippedCosmetics.put(equipRecord.getSlot(), new ProfileCosmeticData(equipRecord));
        }
    }

    @Override
    public boolean has(Cosmetic cosmetic) {
        return player.hasPermission(cosmetic.getPermission());
    }

    @Override
    public void equip(Cosmetic cosmetic) {
        if(isEquipped(cosmetic)) {
            cosmetic.getManager().unequip(this);
        }
        
        final var cosmeticData = new ProfileCosmeticData(cosmetic);
        
        equippedCosmetics.put(cosmetic.getType(), cosmeticData);
        equipped.put(cosmetic.getClass(), new EquippedCosmetic(cosmetic));
        cosmetic.getManager().equip(this);
        
        if(manager.getAdapter() instanceof SqlStorage sqlStorage) {
            sqlStorage.saveCosmeticEquipped(this, cosmetic.getType(), cosmeticData);
        }
    }

    @Override
    public void equip(CosmeticVariant variant) {
        var cosmetic = variant.getCosmetic();
        if(isEquipped(cosmetic)) {
            cosmetic.getManager().unequip(this);
        }

        final var cosmeticData = new ProfileCosmeticData(cosmetic);
        
        equippedCosmetics.put(cosmetic.getType(), cosmeticData);
        equipped.put(cosmetic.getClass(), new EquippedCosmetic(variant));
        cosmetic.getManager().equip(this);
        
        if(manager.getAdapter() instanceof SqlStorage sqlStorage) {
            sqlStorage.saveCosmeticEquipped(this, cosmetic.getType(), cosmeticData);
        }
    }
    
    @Override
    public void equip(Cosmetic cosmetic, Chroma color) {
        if(isEquipped(cosmetic)) {
            cosmetic.getManager().unequip(this);
        }
        
        final var cosmeticData = new ProfileCosmeticData(cosmetic, color);
        
        equippedCosmetics.put(cosmetic.getType(), cosmeticData);
        equipped.put(cosmetic.getClass(), new EquippedCosmetic(cosmetic, color));
        cosmetic.getManager().equip(this);
        
        if(manager.getAdapter() instanceof SqlStorage sqlStorage) {
            sqlStorage.saveCosmeticEquipped(this, cosmetic.getType(), cosmeticData);
        }
    }

    @Override
    public void unequip(Cosmetic cosmetic) {
        unequip(cosmetic.getClass());
    }
    
    @Override
    public void unequip(Class<? extends Cosmetic> tClass) {
        equippedCosmetics.remove(CosmeticType.type(tClass));
        final var pCos = equipped.remove(tClass);
        if(pCos != null) {
            pCos.getCosmetic().getManager().unequip(this);
            
            if(manager.getAdapter() instanceof SqlStorage sqlStorage) {
                sqlStorage.saveCosmeticEquipped(this, pCos.getCosmetic().getType(), null);
            }
        }
    }

    @Override
    public void setHidden(Class<? extends Cosmetic> cosmetic, boolean flag) {
        if(flag) {
            getHidden().add(cosmetic);
        } else {
            getHidden().remove(cosmetic);
        }
    } 

    @Override
    public Optional<EquippedCosmetic> getEquipped(Class<? extends Cosmetic> tClass) {
        return Optional.ofNullable(equipped.get(tClass));
    }
    
    @Override
    public boolean isEquipped(Cosmetic cosmetic) {
        return equipped.containsKey(cosmetic.getClass());
    }

    @Override
    public boolean isHidden(Class<? extends Cosmetic> cosmetic) {
        return getHidden().contains(cosmetic);
    }

    public void reloadCosmetics() {
        equipped.values().forEach(equip -> {
            equip.getCosmetic().getManager().unequip(this);
        });
        
        equipped.clear();
        
        Schedulers.sync().runLater(() -> {
            for(var entry : equippedCosmetics.entrySet()) {
                final var type = entry.getKey();    
                final var data = entry.getValue();
                
                MCCosmeticsPlugin.inst().getCosmetics().getManager(type).ifPresent(manager -> {
                    manager.getCosmetic(data.getId()).ifPresent(cosmetic -> {
                        var c = data.toEquippedCosmetic();
                        equip((Cosmetic) cosmetic);
                    });
                });
            }
        }, 5);
    }

}
