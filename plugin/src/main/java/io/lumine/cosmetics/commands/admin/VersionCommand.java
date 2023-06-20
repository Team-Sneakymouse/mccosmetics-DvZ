package io.lumine.cosmetics.commands.admin;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.cosmetics.nms.VolatileCodeDisabled;
import io.lumine.utils.chat.ColorString;
import io.lumine.utils.commands.Command;
import io.lumine.utils.version.ServerVersion;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class VersionCommand extends Command<MCCosmeticsPlugin> {

    public VersionCommand(AdminCommand plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final String devBuilds = getPlugin().getDescription().getVersion().contains("SNAPSHOT") ? "&aYes" : "&aNo";
        final String preBuilds = getPlugin().isPremium() ? "&aYes" : "&cNo";
        final String supported = (getPlugin().getVolatileCodeHandler() instanceof VolatileCodeDisabled) ? "&cNo" : "&aYes";
        
        String extraInfo = "";

        if(ServerVersion.isPaper()) {
            extraInfo = " &7(Paper)";
        }
        
        String[] messages = {
            ColorString.get("&6Server Version&f: " + Bukkit.getServer().getClass().getPackage().getName() + extraInfo),
            ColorString.get("&6Plugin Version&f: " + getPlugin().getVersion()),
            ColorString.get("&6Plugin Build&f: " + getPlugin().getBuildNumber()),
            ColorString.get("&6Is Premium&f: " + preBuilds),
            ColorString.get("&6Is Dev Build&f: " + devBuilds),
            ColorString.get("&6Supported Version&f: " + supported),
        };
        CommandHelper.sendCommandMessage(sender, messages);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
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
        return "version";
    }
}