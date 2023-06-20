package io.lumine.cosmetics.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.events.extra.ArmorEquipEventListener;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class CosmeticsExecutor extends ReloadableModule<MCCosmeticsPlugin> {

    @Getter private final Map<String,MCCosmeticsManager> cosmeticManagers = Maps.newConcurrentMap();
    
    private final ArmorEquipEventListener armorEquipListener;
    
    public CosmeticsExecutor(MCCosmeticsPlugin plugin) {
        super(plugin);
        
        this.armorEquipListener = new ArmorEquipEventListener();
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {}

    @Override
    public void unload() {
        cosmeticManagers.clear();
    }
    
    public void reloadAllManagers() {
        cosmeticManagers.values().forEach(manager -> manager.reload());
    }
    
    public Optional<MCCosmeticsManager> getManager(String type) {
        return Optional.ofNullable(cosmeticManagers.getOrDefault(type, null));
    }
    
    public <T extends MCCosmeticsManager> void registerCosmeticManager(String type, T manager) {
        type = type.toUpperCase();
        
        if(cosmeticManagers.containsKey(type)) {
            throw new IllegalStateException("Multiple managers cannot be created for the same cosmetic type");
        }
        cosmeticManagers.put(type, manager);
    }

    public Collection<String> getRegisteredTypes() {
        return cosmeticManagers.keySet();
    }
    
    public Collection<String> getRegisteredTypes(Predicate<MCCosmeticsManager> predicate) {
        Collection<String> ret = Lists.newArrayList();
        
        for(var entry : cosmeticManagers.entrySet()) {
            if(predicate.test(entry.getValue())) {
                ret.add(entry.getKey());
            }
        }
        return ret;
    }
    
}
