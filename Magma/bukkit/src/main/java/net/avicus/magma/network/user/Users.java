package net.avicus.magma.network.user;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.ChatColor;
import lombok.Getter;
import net.avicus.compendium.locale.text.UnlocalizedFormat;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.magma.Magma;
import net.avicus.magma.database.model.impl.Rank;
import net.avicus.magma.database.model.impl.User;
import net.avicus.magma.module.prestige.PrestigeModule;
import net.avicus.magma.network.user.rank.BukkitRank;
import net.avicus.magma.network.user.rank.Ranks;
import net.milkbowl.vault.chat.Chat;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Users {

    @Getter
    private static List<User> users = new CopyOnWriteArrayList<>();

    public static void init(CommandsManagerRegistration cmds) {
        Magma.get().getServer().getPluginManager().registerEvents(new UserListener(), Magma.get());
    }

    public static List<User> list() {
        return users;
    }

    public static void join(User user) {
        users.add(user);
    }

    public static void leave(User user) {
        users.remove(user);
    }

    public static Optional<User> user(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public static Optional<User> user(UUID uuid) {
        for (User user : users) {
            if (user.getUniqueId().equals(uuid)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public static User user(CommandSender source) {
        return source instanceof Player ? user(((Player) source).getUniqueId()).get() : User.CONSOLE;
    }

    public static User user(Player player) {
        return user(player.getUniqueId()).orElse(null);
    }

    public static Optional<Player> player(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return player(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Get's Player given a User
     * @param user User to look for Player for
     * @return Optional, ifPresent and isPresent will fail if user is offline or user just doesn't exist.
     */
    public static Optional<Player> player(User user) {
        return Optional.ofNullable(Bukkit.getPlayer(user.getUniqueId()));
    }

    public static Optional<CommandSender> sender(User user) {
        if (user.isConsole()) {
            return Optional.of(Bukkit.getConsoleSender());
        }
        return Optional.ofNullable(player(user).orElse(null));
    }

    public static UnlocalizedText getLocalizedDisplay(User user) {
        return getLocalizedDisplay(user, false);
    }

    @Nullable
    public static UnlocalizedText getPrefix(User user, List<BukkitRank> ranks) {
        String prefix = "";

        for (BukkitRank bukkit : ranks) {
            Rank rank = bukkit.getRank();
            if (rank.getPrefix().isPresent()) {
                prefix = org.bukkit.ChatColor.translateAlternateColorCodes('&', rank.getPrefix().get());
            }

            if (bukkit.getPermissions().contains("magma.rank.highestdisplay")) {
                break;
            }
        }

        if (!prefix.isEmpty()) {
            return new UnlocalizedText(
                    prefix + PrestigeModule.getPrefix(user, !prefix.isEmpty()));
        } else {
            return null;
        }
    }

    @Nullable
    public static UnlocalizedText getSuffix(List<BukkitRank> ranks) {
        String suffix = "";

        for (BukkitRank bukkit : ranks) {
            Rank rank = bukkit.getRank();
            if (rank.getSuffix().isPresent()) {
                suffix = rank.getSuffix().get();
            }

            if (bukkit.getPermissions().contains("magma.rank.highestdisplay")) {
                break;
            }
        }

        if (!suffix.isEmpty()) {
            return new UnlocalizedText(suffix);
        } else {
            return null;
        }
    }

    /**
     * zz
     * Get the display of an online or offline user (rank, badges, team color).
     */
    public static UnlocalizedText getLocalizedDisplay(User user, boolean forceRanks) {
        String name = "";

        Optional<CommandSender> sender = sender(user);
        if (sender.isPresent() && sender.get() instanceof Player) {
            name = name + ChatColor.DARK_AQUA + ((Player) sender.get()).getDisplayName();
        } else if (sender.isPresent()) {
            name = name + ChatColor.DARK_AQUA + "Console";
        } else {
            name = name + ChatColor.DARK_AQUA + user.getName();
        }

        List<BukkitRank> ranks = Ranks.get(user, (sender.isPresent() || !forceRanks));
        Collections.reverse(ranks);

        UnlocalizedText prefix = getPrefix(user, ranks);
        UnlocalizedText suffix = getSuffix(ranks);

        // Prefix Name Suffix
        UnlocalizedFormat format = new UnlocalizedFormat("{0}{1}{2}");
        return format.with(prefix, new UnlocalizedText(name), suffix);
    }

    public static String getDisplay(User user) {
        return getDisplay(user, false);
    }

    public static String getDisplay(User user, boolean forceRanks) {
        return getLocalizedDisplay(user, forceRanks).translate(Locale.ENGLISH).toLegacyText();
    }

    public static User fromSender(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return User.CONSOLE;
        }

        if (sender instanceof Player) {
            return Users.user(((Player) sender).getUniqueId()).get();
        }

        throw new RuntimeException(sender.getName() + " is not a user-assignable sender.");
    }

    public static UnlocalizedText getTranslatableDisplayName(User user, boolean includeRanks) {
        final String name = sender(user)
                .map(source -> source instanceof Player ? ChatColor.DARK_AQUA + ((Player) source)
                        .getDisplayName() : ChatColor.DARK_AQUA + "Console")
                .orElseGet(() -> ChatColor.DARK_AQUA + user.getName());
        String prefix = "";
        String suffix = "";

        if (includeRanks) {
            List<BukkitRank> ranks = Ranks.get(user, false);
            Collections.reverse(ranks);

            // From lowest to highest rank, set the prefix/suffix as we go
            for (BukkitRank bukkit : ranks) {
                Rank rank = bukkit.getRank();
                if (rank.getPrefix().isPresent()) {
                    prefix = org.bukkit.ChatColor.translateAlternateColorCodes('&', rank.getPrefix().get());
                }

                if (rank.getSuffix().isPresent()) {
                    suffix = rank.getSuffix().get();
                }

                if (bukkit.getPermissions().contains("magma.rank.highestdisplay")) {
                    break;
                }
            }
        }

        return new UnlocalizedText(
                prefix + PrestigeModule.getPrefix(user, !prefix.isEmpty()) + name + suffix);
    }

    /**
     * Return a pair of suffix and prefix
     *
     * May be empty strings if Vault is not present, or there is no prefix / suffix system
     *
     * @param p player
     * @return Pair, left - prefix, right - suffix
     */
    public static Pair<String, String> getPrefixSuffix(Player p) {
        Magma magma = Magma.get();
        Optional<Chat> chatHook = magma.getVaultHook().getChat();
        if(chatHook.isPresent()) {
            Chat chat = chatHook.get();
            String prefix = chat.getPlayerPrefix(p);
            String suffix = chat.getPlayerSuffix(p);
            return Pair.of(prefix, suffix);
        } else {
            return Pair.of("", "");
        }
    }
}
