package io.lumine.cosmetics.commands;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.commands.Command;
import io.lumine.utils.serialize.Chroma;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EquipCommand extends Command<MCCosmeticsPlugin> {

    public EquipCommand(Command command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        final var profile = getPlugin().getProfiles().getProfile(player);
        
        var type = args[0];
        var cosmeticName = args[1];

        var maybeManager = getPlugin().getCosmetics().getManager(type);
        if(maybeManager.isEmpty()) {
            CommandHelper.sendError(sender, "Invalid cosmetic type");
            return true;
        }
        
        var manager = maybeManager.get();
        
        var maybeCosmetic = manager.getCosmetic(cosmeticName);
        if(maybeCosmetic.isEmpty()) {
            CommandHelper.sendError(sender, "Specified cosmetic was not found");
            return true;
        }
        var cosmetic = (Cosmetic) maybeCosmetic.get();
        
        if(!profile.has(cosmetic)) {
            CommandHelper.sendError(sender, "You have not unlocked that cosmetic!");
            return true;
        }

        profile.equip(cosmetic);
        CommandHelper.sendSuccess(sender, "Equipped " + CosmeticType.get(manager.getCosmeticClass()).type().toLowerCase() + " " + cosmetic);
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], getPlugin().getCosmetics().getRegisteredTypes(), new ArrayList<>());
        }
        if(args.length == 2) {
            var type = args[0];

            var maybeManager = getPlugin().getCosmetics().getManager(type);
            if(maybeManager.isEmpty()) {
                return Collections.emptyList();
            }
            var manager = maybeManager.get();

            final var player = (Player) sender;
            final var profile = getPlugin().getProfiles().getProfile(player);
            
            Collection<String> cosmeticTypes = Lists.newArrayList();
            for(var cosmetic : (Collection<Cosmetic>) manager.getAllCosmetics()) {
                if(profile.has(cosmetic)) {
                    cosmeticTypes.add(cosmetic.getId());
                }
            }
            
            return StringUtil.copyPartialMatches(args[1], cosmeticTypes, new ArrayList<>());
        }
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_BASE;
    }

    @Override
    public boolean isConsoleFriendly() {
        return false;
    }

    @Override
    public String getName() {
        return "equip";
    }
}
