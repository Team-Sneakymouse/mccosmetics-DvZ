package io.lumine.cosmetics.commands;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
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

public class DyeCommand extends Command<MCCosmeticsPlugin> {

    public DyeCommand(Command command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        final var profile = getPlugin().getProfiles().getProfile(player);
        
        var type = args[0];
        Chroma color;
        try {
            color = Chroma.of(args[1]);
        } catch(Exception | Error ex) {
            ex.printStackTrace();
            CommandHelper.sendError(sender, "Invalid dye color");
            return true;
        }
        
        var maybeManager = getPlugin().getCosmetics().getManager(type);
        if(maybeManager.isEmpty()) {
            CommandHelper.sendError(sender, "Invalid cosmetic type");
            return true;
        }
        
        var manager = maybeManager.get();
        
        Optional<EquippedCosmetic> maybeEquipped = profile.getEquipped(manager.getCosmeticClass());
        if(maybeEquipped.isEmpty()) {
            CommandHelper.sendError(sender, "You don't have that type of cosmetic equipped!");
            return true;
        }
        
        var equipped = maybeEquipped.get();

        if(!(equipped.getCosmetic() instanceof ColorableCosmetic colorable) || !colorable.isColorable()) {
            CommandHelper.sendError(sender, "The cosmetic you're wearing isn't dyeable!");
            return true;
        }

        profile.equip(equipped.getCosmetic(), color);
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], getPlugin().getCosmetics().getRegisteredTypes((man) -> man.getClass().isAssignableFrom(ColorableCosmetic.class)), new ArrayList<>());
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
        return "dye";
    }
}
