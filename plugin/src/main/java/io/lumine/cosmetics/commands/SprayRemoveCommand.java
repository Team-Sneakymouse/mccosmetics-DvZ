package io.lumine.cosmetics.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.MCCosmetics;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.cosmetics.managers.sprays.Spray;
import io.lumine.utils.commands.Command;

public class SprayRemoveCommand extends Command<MCCosmeticsPlugin> {

    public SprayRemoveCommand(MCCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        var player = (Player) sender;
        getPlugin().getSprayManager().removeSpray(player);
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_SPRAY;
    }

    @Override
    public boolean isConsoleFriendly() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }
}