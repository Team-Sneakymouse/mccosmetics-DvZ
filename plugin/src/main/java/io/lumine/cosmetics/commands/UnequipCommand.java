package io.lumine.cosmetics.commands;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.commands.Command;
import io.lumine.utils.serialize.Chroma;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UnequipCommand extends Command<MCCosmeticsPlugin> {

    public UnequipCommand(Command command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        final var profile = getPlugin().getProfiles().getProfile(player);

        var type = args[0];

        var maybeManager = getPlugin().getCosmetics().getManager(type);
        if(maybeManager.isEmpty()) {
            CommandHelper.sendError(sender, "Invalid cosmetic type");
            return true;
        }

        var manager = maybeManager.get();

        profile.unequip(manager.getCosmeticClass());
        //CommandHelper.sendSuccess(sender, "Unequipped your " + CosmeticType.get(manager.getCosmeticClass()).type().toLowerCase());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], getPlugin().getCosmetics().getRegisteredTypes(), new ArrayList<>());
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
        return "unequip";
    }
}
