package io.lumine.cosmetics.commands.admin;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.commands.Command;
import io.lumine.utils.text.Text;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AdminCommand extends Command<MCCosmeticsPlugin> {

    public AdminCommand(MCCosmeticsPlugin plugin) {
        super(plugin);
        
        addSubCommands(
                new ReloadCommand(this),
                new SprayCommand(this),
                new VersionCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Component[] messages = {
                Text.parse("&e/mcc &areload &7\u25BA &7&oReloads everything"),
                Text.parse("&e/mcc &aversion &7\u25BA &7&oPlugin version information")
              };
        CommandHelper.sendCommandMessage(sender, messages);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_ADMIN;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
