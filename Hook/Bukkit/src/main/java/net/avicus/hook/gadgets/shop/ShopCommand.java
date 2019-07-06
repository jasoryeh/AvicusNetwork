package net.avicus.hook.gadgets.shop;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import net.avicus.compendium.commands.exception.MustBePlayerCommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand {

    @Command(aliases = {"backpack", "bp"}, desc = "Open your gadget backpack.", max = 0)
    public static void backpack(CommandContext context, CommandSender sender)
            throws MustBePlayerCommandException {
        MustBePlayerCommandException.ensurePlayer(sender);

        Player player = (Player) sender;
        ShopMenu menu = new ShopMenu(player);
        menu.open();
    }
}
