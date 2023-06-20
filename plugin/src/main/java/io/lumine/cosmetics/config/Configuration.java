package io.lumine.cosmetics.config;

import com.google.common.collect.Lists;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.storage.StorageDriver;
import io.lumine.utils.storage.sql.SqlCredentials;
import io.lumine.utils.config.properties.types.SqlCredentialsProp;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.config.properties.types.EnumProp;
import io.lumine.utils.config.properties.types.IntProp;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Configuration extends ReloadableModule<MCCosmeticsPlugin> implements PropertyHolder {
    
    private static final IntProp CLOCK_INTERVAL = Property.Int(Scope.CONFIG, "Clock.Interval", 1);
    private static final EnumProp<StorageDriver> STORAGE_DRIVER = Property.Enum(Scope.CONFIG, StorageDriver.class, "Storage.Driver", StorageDriver.JSON); 
    private static final SqlCredentialsProp SQL_CREDENTIALS = Property.SqlCredentials(Scope.CONFIG, "Storage");
    
    private static final IntProp EQUIP_DELAY = Property.Int(Scope.CONFIG, "General.EquipDelay", 5);
    
    @Getter private boolean allowingMetrics = true;
    
    public Configuration(MCCosmeticsPlugin plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MCCosmeticsPlugin plugin) {
        Log.info("Loading Configuration...");
        generateDefaultConfigFiles();
    }
  
    @Override
    public void unload() {}

    @Override
    public String getPropertyNode() {
        return "Configuration";
    }
    
    public Collection<File> getPackFolders() {
        final File packsFolder = new File(getPlugin().getDataFolder() + System.getProperty("file.separator") + "packs");
        final Collection<File> packs = Lists.newArrayList();
        
        if(!packsFolder.exists()) {
            packsFolder.mkdir();
        }
        if(packsFolder.exists() && packsFolder.isDirectory()) {
            for(var packFolder : packsFolder.listFiles()) {
                if(packFolder.isDirectory()) {
                    packs.add(packFolder);
                }
            }
        }
        return packs;
    }
    
    public int getClockInterval() {
        return CLOCK_INTERVAL.get(this);
    }
    
    public StorageDriver getStorageType() {
        return STORAGE_DRIVER.get(this);
    }
    
    public SqlCredentials getSqlCredentials() {
        return SQL_CREDENTIALS.get(this);
    }
    
    public int getEquipDelay() {
        return EQUIP_DELAY.get(this);
    }
    
    private void generateDefaultConfigFiles() {
        final var menuFolder = new File(plugin.getDataFolder(), "menus");
        final var packFolder = new File(plugin.getDataFolder(), "packs");
        final var demoFolder = new File(packFolder, "starter");
        
        final String copyFolder;
        if(getPlugin().isPremium()) {
            copyFolder = "premium";
        } else {
            copyFolder = "default";
        }
        
        if(!menuFolder.exists()) {
            Log.info("Generating Menu files...");

            if(menuFolder.mkdir()) {
                try {
                    JarFile jarFile = new JarFile(getPlugin().getJarFile());
                    for(Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                        String name = entries.nextElement().getName();
                        if(name.startsWith(copyFolder + "/menus/") && name.length() > (copyFolder + "/menus/").length()) {
                            Files.copy(getPlugin().getResource(name), new File(getPlugin().getDataFolder() + "/menus", name.split("/")[2]).toPath());
                        }
                    }
                    jarFile.close();
                } catch(IOException ex) {
                    Log.error("Could not load default menu configuration.");
                    ex.printStackTrace();
                }
            } else Log.error("Could not create directory!");
        }
        
        if(!packFolder.exists()) {
            if(!packFolder.mkdir()) {
                Log.error("Could not create packs directory!");
            }
        } 
        
        if(!demoFolder.exists()) {
            Log.info("Generating Starter Pack files...");

            if(demoFolder.mkdir()) {
                try {
                    JarFile jarFile = new JarFile(getPlugin().getJarFile());
                    for(Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                        String name = entries.nextElement().getName();
                        if(name.startsWith(copyFolder + "/packs/") && name.length() > (copyFolder + "/packs/").length()) {
                            Files.copy(getPlugin().getResource(name), new File(getPlugin().getDataFolder() + "/packs", name.split("/")[2]).toPath());
                        }
                    }
                    jarFile.close();
                } catch(IOException ex) {
                    Log.error("Could not load starter pack files.");
                    ex.printStackTrace();
                }
            } else Log.error("Could not create directory!");
        }
    }
    
}
