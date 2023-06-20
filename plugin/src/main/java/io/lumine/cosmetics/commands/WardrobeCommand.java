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

public class WardrobeCommand extends Command<MCCosmeticsPlugin> {

    public WardrobeCommand(Command command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        
        var manager = getPlugin().getWardrobeManager();
        
        if(manager.isInWardrobe(player)) {
            manager.closeWardrobe(player);
        } else {
            manager.openWardrobe(player);
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
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
        return "wardrobe";
    }
}
