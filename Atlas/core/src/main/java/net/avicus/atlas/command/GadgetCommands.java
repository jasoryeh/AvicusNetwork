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
import net.avicus.magma.network.user.Users;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class GadgetCommands {
    @Command(aliases = {"gadget", "gadgets"}, desc = "Manage Atlas gadgets.", min = 0, max = -1)
    public static void gadget(CommandContext cmd, final CommandSender sender) {
        sender.sendMessage("/gadget [give] [key|crate|maptoken] [uuid or username] [other e.g. type/alpha/beta/etc.]");

        User user = null;
        if (cmd.argsLength() >= 3) {
            String arg3 = cmd.getString(2);
            if (arg3.contains("-")) {
                Optional<User> oUser = Users.user(UUID.fromString(arg3));
                if (oUser.isPresent()) {
                    user = oUser.get();
                }
            }

            Player player = Bukkit.getPlayer(arg3);
            if (player != null) {
                Optional<User> user1 = Users.user(player.getUniqueId());
                if (user1.isPresent()) {
                    user = user1.get();
                }
            }
        }

        if (user == null) {
            sender.sendMessage("Invalid user.");
            return;
        }

        if (cmd.getString(0).equalsIgnoreCase("give")) {
            if (cmd.getString(1).equalsIgnoreCase("key")) {
                Gadget token = new KeyGadget(TypeManager.getType(cmd.getString(3)));
                Magma.get().getMm().get(Gadgets.class).createBackpackGadget(user, token.defaultContext(), true, new Date());
                sender.sendMessage("Successfully added.");
            } else if (cmd.getString(1).equalsIgnoreCase("crate")) {
                Gadget token = new CrateGadget(TypeManager.getType(cmd.getString(3)));
                Magma.get().getMm().get(Gadgets.class).createBackpackGadget(user, token.defaultContext(), true, new Date());
                sender.sendMessage("Successfully added.");
            } else if (cmd.getString(1).equalsIgnoreCase("maptoken")) {
                Gadget token = new MapSetNextToken();
                Magma.get().getMm().get(Gadgets.class).createBackpackGadget(user, token.defaultContext(), true, new Date());
                sender.sendMessage("Successfully added.");
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid/Unsupported gadget.");
            }
        }
    }

}
