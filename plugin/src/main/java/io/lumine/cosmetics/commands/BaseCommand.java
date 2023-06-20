package io.lumine.cosmetics.commands;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BaseCommand extends Command<MCCosmeticsPlugin> {

    public BaseCommand(MCCosmeticsPlugin plugin) {
        super(plugin);
        
        this.addSubCommands(
                new DyeCommand(this), 
                new EquipCommand(this), 
                new UnequipCommand(this),
                new WardrobeCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        final var profile = getPlugin().getProfiles().getProfile(player);
        
        if(profile == null) {
            throw new IllegalStateException("This should never happen, please report to the developers.");
        }
        
        getPlugin().getMenuManager().getCustomizeMenu().open(player,profile);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
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
        return null;
    }
}
