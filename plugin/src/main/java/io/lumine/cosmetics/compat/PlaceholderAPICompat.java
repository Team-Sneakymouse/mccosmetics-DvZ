package io.lumine.cosmetics.compat;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.Profile;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPICompat extends PlaceholderExpansion {

    private final MCCosmeticsPlugin plugin;

    public PlaceholderAPICompat(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        Profile profile = plugin.getProfiles().getProfile(player);

        if(identifier.startsWith("is_equipped_")){
            String cosmeticType = identifier.split("_")[2].toUpperCase();
            return profile.getEquippedCosmetics().containsKey(cosmeticType) ? "true" : "false";
        }

        if(identifier.startsWith("current_")){
            String cosmeticType = identifier.split("_")[1].toUpperCase();

            if(!profile.getEquippedCosmetics().containsKey(cosmeticType)){
                return "NONE";
            } else{
                return profile.getEquippedCosmetics().get(cosmeticType).getId();
            }

        }

        switch (identifier) {
            case "is_gesturing":
                return profile.getEquippedCosmetics().containsKey("GESTURE") ? "true" : "false";
            case "active_gesture":
                if(!profile.getEquippedCosmetics().containsKey("GESTURE")) {
                    return "None";
                } else {
                    return profile.getEquippedCosmetics().get("GESTURE").getId();
                }
            default:
                return null;
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mcc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lumine Studios";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
}
