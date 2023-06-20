package io.lumine.cosmetics.commands;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CustomizeCommand extends Command<MCCosmeticsPlugin> {

    public CustomizeCommand(MCCosmeticsPlugin plugin) {
        super(plugin);
    }
    
    public CustomizeCommand(Command command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        final var profile = getPlugin().getProfiles().getProfile(player);
        getPlugin().getMenuManager().getCustomizeMenu().open(player,profile);
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
        return "customize";
    }
}
