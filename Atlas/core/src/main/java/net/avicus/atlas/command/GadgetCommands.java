package net.avicus.atlas.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.module.gadget.map.MapSetNextToken;
import net.avicus.atlas.module.gadget.map.MapTokenContext;
import net.avicus.magma.Magma;
import net.avicus.magma.database.model.impl.User;
import net.avicus.magma.module.gadgets.Gadget;
import net.avicus.magma.module.gadgets.Gadgets;
import net.avicus.magma.module.gadgets.crates.CrateGadget;
import net.avicus.magma.module.gadgets.crates.KeyGadget;
import net.avicus.magma.module.gadgets.crates.TypeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class GadgetCommands {
    @Command(aliases = {"gadget", "gadgets"}, desc = "Manage Atlas gadgets.", min = 0, max = -1)
    public static void gadget(CommandContext cmd, final CommandSender sender) {

        User user = null;
        if (cmd.argsLength() >= 3) {
            String arg3 = cmd.getString(2);
            if (arg3.contains("-")) {
                Optional<User> oUser = Magma.get().database().getUsers().findByUuid(UUID.fromString(arg3));
                if (oUser.isPresent()) {
                    user = oUser.get();
                }
            }

            Optional<User> oUser = Magma.get().database().getUsers().findByName(arg3);
            if (oUser.isPresent()) {
                user = oUser.get();
            }
        }

        if (user == null) {
            sender.sendMessage(ChatColor.RED + "/gadget [give] [key|crate|maptoken] [uuid or username] [other e.g. type/alpha/beta/etc.]");
            sender.sendMessage("Invalid user specified.");
            return;
        }

        if (cmd.getString(0).equalsIgnoreCase("give")) {
            if (cmd.getString(1).equalsIgnoreCase("key")) {
                Gadget token = new KeyGadget(TypeManager.getType(cmd.getString(3)));
                Magma.get().getMm().get(Gadgets.class).createBackpackGadget(user, token.defaultContext(), true, new Date());
                sender.sendMessage("Successfully added key.");
            } else if (cmd.getString(1).equalsIgnoreCase("crate")) {
                Gadget token = new CrateGadget(TypeManager.getType(cmd.getString(3)));
                Magma.get().getMm().get(Gadgets.class).createBackpackGadget(user, token.defaultContext(), true, new Date());
                sender.sendMessage("Successfully added crate.");
            } else if (cmd.getString(1).equalsIgnoreCase("maptoken")) {
                Gadget token = new MapSetNextToken();
                Magma.get().getMm().get(Gadgets.class).createBackpackGadget(user, token.defaultContext(), true, new Date());
                sender.sendMessage("Successfully added map token.");
            } else {
                sender.sendMessage(ChatColor.RED + "/gadget [give] [key|crate|maptoken] [uuid or username] [other e.g. type/alpha/beta/etc.]");
                sender.sendMessage(ChatColor.RED + "Invalid/Unsupported gadget.");
            }
        }
    }

}
