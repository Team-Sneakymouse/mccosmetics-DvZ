package io.lumine.cosmetics.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.cosmetics.manager.CosmeticManager;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.api.players.wardrobe.Mannequin;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.menus.SelectionMenu;
import io.lumine.cosmetics.nms.VolatileCodeHandler;
import io.lumine.cosmetics.nms.cosmetic.VolatileCosmeticHelper;
import io.lumine.cosmetics.players.ProfileManager;
import io.lumine.cosmetics.players.wardrobe.WardrobeExecutor;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.NodeListProp;
import io.lumine.utils.files.Files;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class MCCosmeticsManager<T extends Cosmetic> extends ReloadableModule<MCCosmeticsPlugin> implements CosmeticManager {

    protected final NodeListProp KEYS = Property.NodeList(Scope.NONE, "");
    protected final Map<String, T> cosmetics = Maps.newConcurrentMap();
    @Getter protected final Class<T> cosmeticClass;
    
    @Getter protected SelectionMenu menu = null;

    public MCCosmeticsManager(MCCosmeticsPlugin plugin, Class<T> tClass) {
        super(plugin, false); 
        this.cosmeticClass = tClass;
        
        plugin.getCosmetics().registerCosmeticManager(CosmeticType.type(cosmeticClass), this);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        final Collection<File> files = Lists.newArrayList();
        final String type = CosmeticType.folder(cosmeticClass);
        for(var packFolder : plugin.getConfiguration().getPackFolders()) {
            final File confFolder = new File(packFolder.getAbsolutePath() + System.getProperty("file.separator") + type);
            if(confFolder.exists() && confFolder.isDirectory()) {
                files.addAll(Files.getAllYaml(confFolder.getAbsolutePath()));
            }
        }

        for(var file : files) {
            for(var node : KEYS.fget(file)) {
                var cosmetic = build(file, node);
                if(!cosmetics.containsKey(cosmetic.getId())){
                    cosmetics.put(cosmetic.getId(), cosmetic);
                } else {
                    Log.warn("Cosmetic with id " + cosmetic.getId() + " already exists!");
                    Log.warn("Cosmetic will be ignored! Please rename this cosmetic.");
                }

            }
        }

        if(menu == null) {
            var menuFile = new File(plugin.getDataFolder(), "menus/selection_" + type + ".yml");
            
            if(menuFile.exists()) {
                Log.info("Found custom selection menu for {0}", type);
                menu = new SelectionMenu(plugin, plugin.getMenuManager(), this, type);
                menu.reload();
            }
        } else {
            menu.reload();
        }
        Log.info("Loaded " + cosmetics.size() + " " + type + ".");
    }

    @Override
    public void unload() {
        cosmetics.clear();
    }

    public abstract T build(File file, String node);

    public Collection<T> getAllCosmetics() {
        return cosmetics.values();
    }

    public Optional<T> getCosmetic(String key) {
        return Optional.ofNullable(cosmetics.getOrDefault(key, null));
    }

    public void equip(Player player) {
        equip(getProfiles().getProfile(player));
    }

    public abstract void equip(CosmeticProfile profile);

    @Override
    public void equipMannequin(Mannequin mannequin, EquippedCosmetic cosmetic) {
        
    }
    
    @Override
    public void unequipMannequin(Mannequin mannequin) {
        
    }
    
    protected ProfileManager getProfiles() {
        return plugin.getProfiles();
    }
    
    protected VolatileCodeHandler getNMS() {
        return plugin.getVolatileCodeHandler();
    }
    
    protected VolatileCosmeticHelper getNMSHelper() {
        return getNMS().getCosmeticHelper(cosmeticClass);
    }
    
    protected WardrobeExecutor getWardrobeManager() {
        return plugin.getWardrobeManager();
    }

}
