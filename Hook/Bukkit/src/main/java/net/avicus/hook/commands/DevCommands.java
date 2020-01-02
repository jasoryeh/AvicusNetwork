package net.avicus.hook.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import net.avicus.compendium.config.Config;
import net.avicus.compendium.config.ConfigFile;
import net.avicus.hook.HookPlugin;
import net.avicus.hook.backend.Backend;
import net.avicus.hook.backend.BackendConfig;
import net.avicus.hook.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;

public class DevCommands {

    @Command(aliases = {
            "permissions"}, desc = "View permissions for a player and which plugin assigned them.", max = 1)
    public static void permissions(CommandContext cmd, CommandSender sender) {
        String query = cmd.getString(0);
        Player search = Bukkit.getPlayer(query);

        if (search == null) {
            sender.sendMessage(Messages.ERROR_NO_PLAYERS.with(ChatColor.RED));
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Permissions for " + search.getDisplayName());
        search.getEffectivePermissions().forEach(attachment -> {
            if (attachment.getAttachment() == null) {
                return;
            }

            String assigner = "Bukkit";
            if (attachment.getAttachment().getPlugin() != null) {
                assigner = attachment.getAttachment().getPlugin().getName();
            }
            String permission = attachment.getPermission();
            ChatColor color = attachment.getValue() ? ChatColor.GREEN : ChatColor.RED;

            sender.sendMessage(ChatColor.AQUA + assigner + " - " + color + permission);
        });
    }

    @Command(aliases = {"has-permission",
            "hp"}, desc = "Check if a player has a permission.", max = 2, usage = "<player> <permission>")
    public static void hasPerm(CommandContext cmd, CommandSender sender) {
        String query = cmd.getString(0);
        Player search = Bukkit.getPlayer(query);

        if (search == null) {
            sender.sendMessage(Messages.ERROR_NO_PLAYERS.with(ChatColor.RED));
            return;
        }

        sender.sendMessage(
                search.hasPermission(cmd.getString(1)) ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO");
    }

    @Command(aliases = {"update-backend"}, desc = "Force updates a backend task.")
    public static void updateBackend(CommandContext cmd, CommandSender sender) {
        HookPlugin plugin = HookPlugin.getInstance();

        File remote = new File(plugin.getDataFolder(), "backend.yml");
        InputStream local = plugin.getResource("backend.yml");

        Config config = new Config(local);

        if (!remote.exists()) {
            config.save(remote);
        }
        config = new ConfigFile(remote);

        config.injector(BackendConfig.class).inject();

        Backend backend = new Backend(plugin);

        // Backend start equiv
        try {
            boolean allDone = true;

            for (Thread thread : backend.runBackEnd()) {
                if (thread.isAlive()) {
                    allDone = false;
                    break;
                }
            }
            sender.sendMessage("Backend task finished.");
        } catch (Exception e) {
            sender.sendMessage("Backend task failed.");
            e.printStackTrace();
        }
    }

    public static class Parent {

        @CommandPermissions("hook.dev")
        @Command(aliases = {"dev"}, desc = "Development commands")
        @NestedCommand(DevCommands.class)
        public static void parent(CommandContext args, CommandSender source) {
        }
    }
}
